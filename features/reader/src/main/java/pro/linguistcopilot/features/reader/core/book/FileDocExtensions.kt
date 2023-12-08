@file:Suppress("unused")

package pro.linguistcopilot.features.reader.core.book

import android.app.DownloadManager
import android.content.Context
import android.database.Cursor
import android.net.Uri
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract
import androidx.documentfile.provider.DocumentFile
import splitties.init.appCtx
import splitties.systemservices.downloadManager
import java.io.File
import java.io.FileOutputStream
import java.io.InputStream
import java.io.OutputStream
import java.nio.charset.Charset

@Throws(Exception::class)
fun Uri.readBytes(context: Context): ByteArray {
    return if (this.isContentScheme()) {
        context.contentResolver.openInputStream(this)?.let {
            val len: Int = it.available()
            val buffer = ByteArray(len)
            it.read(buffer)
            it.close()
            return buffer
        } ?: throw RuntimeException("打开文件失败\n${this}")
    } else {
        val path = RealPathUtil.getPath(context, this)
        if (path?.isNotEmpty() == true) {
            File(path).readBytes()
        } else {
            throw RuntimeException("获取文件真实地址失败\n${this.path}")
        }
    }
}

data class FileDoc(
    val name: String,
    val isDir: Boolean,
    val size: Long,
    val lastModified: Long,
    val uri: Uri
) {

    override fun toString(): String {
        return if (uri.isContentScheme()) uri.toString() else uri.path!!
    }

    val isContentScheme get() = uri.isContentScheme()

    fun readBytes(): ByteArray {
        return uri.readBytes(appCtx)
    }

    fun asDocumentFile(): DocumentFile? {
        if (isContentScheme) {
            return if (isDir) {
                Class.forName("androidx.documentfile.provider.TreeDocumentFile")
                    .getDeclaredConstructor(
                        DocumentFile::class.java,
                        Context::class.java,
                        Uri::class.java
                    ).apply {
                        isAccessible = true
                    }.newInstance(null, appCtx, uri) as DocumentFile
            } else {
                DocumentFile.fromSingleUri(appCtx, uri)
            }
        }
        return null
    }

    fun asFile(): File? {
        if (isContentScheme) {
            return null
        }
        return File(uri.path!!)
    }

    companion object {

        fun fromUri(uri: Uri, isDir: Boolean): FileDoc {
            if (uri.isContentScheme()) {
                val doc = if (isDir) {
                    DocumentFile.fromTreeUri(appCtx, uri)!!
                } else if (uri.host == "downloads") {
                    val query = DownloadManager.Query()
                    query.setFilterById(uri.lastPathSegment!!.toLong())
                    downloadManager.query(query).use {
                        if (it.moveToFirst()) {
                            val lUriColum = it.getColumnIndex(DownloadManager.COLUMN_LOCAL_URI)
                            val lUri = it.getString(lUriColum)
                            DocumentFile.fromSingleUri(appCtx, Uri.parse(lUri))!!
                        } else {
                            DocumentFile.fromSingleUri(appCtx, uri)!!
                        }
                    }
                } else {
                    DocumentFile.fromSingleUri(appCtx, uri)!!
                }
                return FileDoc(doc.name ?: "", isDir, doc.length(), doc.lastModified(), doc.uri)
            }
            val file = File(uri.path!!)
            return FileDoc(file.name, isDir, file.length(), file.lastModified(), uri)
        }

        fun fromDocumentFile(doc: DocumentFile): FileDoc {
            return FileDoc(
                name = doc.name ?: "",
                isDir = doc.isDirectory,
                size = doc.length(),
                lastModified = doc.lastModified(),
                uri = doc.uri
            )
        }

        fun fromFile(file: File): FileDoc {
            return FileDoc(
                name = file.name,
                isDir = file.isDirectory,
                size = file.length(),
                lastModified = file.lastModified(),
                uri = Uri.fromFile(file)
            )
        }

    }
}

/**
 * 过滤器
 */
typealias FileDocFilter = (file: FileDoc) -> Boolean

private val projection by lazy {
    arrayOf(
        DocumentsContract.Document.COLUMN_DOCUMENT_ID,
        DocumentsContract.Document.COLUMN_DISPLAY_NAME,
        DocumentsContract.Document.COLUMN_LAST_MODIFIED,
        DocumentsContract.Document.COLUMN_SIZE,
        DocumentsContract.Document.COLUMN_MIME_TYPE
    )
}

/**
 * 返回子文件列表,如果不是文件夹则返回null
 */
fun FileDoc.list(filter: FileDocFilter? = null): ArrayList<FileDoc>? {
    if (isDir) {
        if (uri.isContentScheme()) {
            /**
             * DocumentFile 的 listFiles() 非常的慢,所以这里直接从数据库查询
             */
            val childrenUri = DocumentsContract
                .buildChildDocumentsUriUsingTree(uri, DocumentsContract.getDocumentId(uri))
            val docList = arrayListOf<FileDoc>()
            var cursor: Cursor? = null
            try {
                cursor = appCtx.contentResolver.query(
                    childrenUri,
                    projection,
                    null,
                    null,
                    DocumentsContract.Document.COLUMN_DISPLAY_NAME
                )
                cursor?.let {
                    val ici = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DOCUMENT_ID)
                    val nci = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_DISPLAY_NAME)
                    val sci = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_SIZE)
                    val mci = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_MIME_TYPE)
                    val dci = cursor.getColumnIndex(DocumentsContract.Document.COLUMN_LAST_MODIFIED)
                    if (cursor.moveToFirst()) {
                        do {
                            val item = FileDoc(
                                name = cursor.getString(nci),
                                isDir = cursor.getString(mci) == DocumentsContract.Document.MIME_TYPE_DIR,
                                size = cursor.getLong(sci),
                                lastModified = cursor.getLong(dci),
                                uri = DocumentsContract.buildDocumentUriUsingTree(
                                    uri,
                                    cursor.getString(ici)
                                )
                            )
                            if (filter == null || filter.invoke(item)) {
                                docList.add(item)
                            }
                        } while (cursor.moveToNext())
                    }
                }
            } finally {
                cursor?.close()
            }
            return docList
        } else {
            return File(uri.path!!).listFileDocs(filter)
        }
    }
    return null
}

fun File.listFileDocs(filter: FileDocFilter? = null): ArrayList<FileDoc> {
    val docList = arrayListOf<FileDoc>()
    listFiles()?.forEach {
        val item = FileDoc(
            it.name,
            it.isDirectory,
            it.length(),
            it.lastModified(),
            Uri.fromFile(it)
        )
        if (filter == null || filter.invoke(item)) {
            docList.add(item)
        }
    }
    return docList
}

/**
 * 查找文档, 如果存在则返回文档,如果不存在返回空
 * @param name 文件名
 * @param depth 查找文件夹深度
 */
fun FileDoc.find(name: String, depth: Int = 0): FileDoc? {
    val list = list()
    list?.forEach {
        if (it.name == name) {
            return it
        }
    }
    if (depth > 0) {
        list?.forEach {
            if (it.isDir) {
                val fileDoc = it.find(name, depth - 1)
                if (fileDoc != null) {
                    return fileDoc
                }
            }
        }
    }
    return null
}

fun FileDoc.createFileIfNotExist(
    fileName: String,
    vararg subDirs: String
): FileDoc {
    return if (uri.isContentScheme()) {
        val documentFile = asDocumentFile()!!
        val tmp = DocumentUtils.createFileIfNotExist(documentFile, fileName, *subDirs)!!
        FileDoc.fromDocumentFile(tmp)
    } else {
        val path = FileUtils.getPath(uri.path!!, *subDirs) + File.separator + fileName
        val tmp = FileUtils.createFileIfNotExist(path)
        FileDoc.fromFile(tmp)
    }
}

fun Uri.outputStream(context: Context): Result<OutputStream> {
    val uri = this
    return kotlin.runCatching {
        try {
            if (isContentScheme()) {
                DocumentFile.fromSingleUri(context, uri)
                    ?: throw RuntimeException("未获取到文件")
                return@runCatching context.contentResolver.openOutputStream(uri)!!
            } else {
                val path = RealPathUtil.getPath(context, uri)
                    ?: throw RuntimeException("未获取到文件")
                val file = File(path)
                if (file.exists()) {
                    return@runCatching FileOutputStream(file)
                } else {
                    throw RuntimeException("文件不存在")
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }
}


fun FileDoc.createFolderIfNotExist(
    vararg subDirs: String
): FileDoc {
    return if (uri.isContentScheme()) {
        val documentFile = asDocumentFile()!!
        val tmp = DocumentUtils.createFolderIfNotExist(documentFile, *subDirs)!!
        FileDoc.fromDocumentFile(tmp)
    } else {
        val path = FileUtils.getPath(uri.path!!, *subDirs)
        val tmp = FileUtils.createFolderIfNotExist(path)
        FileDoc.fromFile(tmp)
    }
}

fun FileDoc.openInputStream(): Result<InputStream> {
    return uri.inputStream(appCtx)
}

fun FileDoc.openOutputStream(): Result<OutputStream> {
    return uri.outputStream(appCtx)
}

fun FileDoc.openReadPfd(): Result<ParcelFileDescriptor> {
    return uri.toReadPfd(appCtx)
}

fun Uri.toReadPfd(context: Context): Result<ParcelFileDescriptor> {
    val uri = this
    return kotlin.runCatching {
        try {
            if (isContentScheme()) {
                DocumentFile.fromSingleUri(context, uri)
                    ?: throw RuntimeException("未获取到文件")
                return@runCatching context.contentResolver.openFileDescriptor(uri, "r")!!
            } else {
                val path = RealPathUtil.getPath(context, uri)
                    ?: throw RuntimeException("未获取到文件")
                val file = File(path)
                if (file.exists()) {
                    return@runCatching ParcelFileDescriptor.open(
                        file,
                        ParcelFileDescriptor.MODE_READ_ONLY
                    )
                } else {
                    throw RuntimeException("文件不存在")
                }
            }


        } catch (e: Exception) {
            throw e
        }
    }
}

fun FileDoc.openWritePfd(): Result<ParcelFileDescriptor> {
    return uri.toWritePfd(appCtx)
}

fun Uri.toWritePfd(context: Context): Result<ParcelFileDescriptor> {
    val uri = this
    return kotlin.runCatching {
        try {
            if (isContentScheme()) {
                DocumentFile.fromSingleUri(context, uri)
                    ?: throw RuntimeException("未获取到文件")
                return@runCatching context.contentResolver.openFileDescriptor(uri, "w")!!
            } else {
                val path = RealPathUtil.getPath(context, uri)
                    ?: throw RuntimeException("未获取到文件")
                val file = File(path)
                if (file.exists()) {
                    return@runCatching ParcelFileDescriptor.open(
                        file,
                        ParcelFileDescriptor.MODE_WRITE_ONLY
                    )
                } else {
                    throw RuntimeException("文件不存在")
                }
            }


        } catch (e: Exception) {
            throw e
        }
    }
}

fun FileDoc.exists(
    fileName: String,
    vararg subDirs: String
): Boolean {
    return if (uri.isContentScheme()) {
        DocumentUtils.exists(asDocumentFile()!!, fileName, *subDirs)
    } else {
        val path = FileUtils.getPath(uri.path!!, *subDirs) + File.separator + fileName
        FileUtils.exist(path)
    }
}

fun FileDoc.exists(): Boolean {
    return if (uri.isContentScheme()) {
        asDocumentFile()!!.exists()
    } else {
        FileUtils.exist(uri.path!!)
    }
}

fun FileDoc.writeText(text: String) {
    if (uri.isContentScheme()) {
        uri.writeText(appCtx, text)
    } else {
        File(uri.path!!).writeText(text)
    }
}
@Throws(Exception::class)
fun Uri.writeText(context: Context, text: String, charset: Charset = Charsets.UTF_8): Boolean {
    return writeBytes(context, text.toByteArray(charset))
}
@Throws(Exception::class)
fun Uri.writeBytes(
    context: Context,
    byteArray: ByteArray
): Boolean {
    if (this.isContentScheme()) {
        context.contentResolver.openOutputStream(this)?.let {
            it.write(byteArray)
            it.close()
            return true
        }
        return false
    } else {
        val path = RealPathUtil.getPath(context, this)
        if (path?.isNotEmpty() == true) {
            File(path).writeBytes(byteArray)
            return true
        }
    }
    return false
}
fun FileDoc.delete() {
    asFile()?.let {
        FileUtils.delete(it, true)
    }
    asDocumentFile()?.delete()
}

fun FileDoc.checkWrite(): Boolean? {
    if (!isDir) {
        throw RuntimeException("只能检查目录")
    }
    asFile()?.let {
        return it.checkWrite()
    }
    return asDocumentFile()?.checkWrite()
}

fun File.checkWrite(): Boolean {
    return try {
        val filename = System.currentTimeMillis().toString()
        val file = FileUtils.createFileIfNotExist(this, filename)
        file.outputStream().use { }
        file.delete()
        true
    } catch (e: Exception) {
        false
    }
}

/**
 * DocumentFile 的 listFiles() 非常的慢,尽量不要使用
 */
fun DocumentFile.listFileDocs(filter: FileDocFilter? = null): ArrayList<FileDoc>? {
    return FileDoc.fromDocumentFile(this).list(filter)
}

@Throws(Exception::class)
fun DocumentFile.openInputStream(): InputStream? {
    return appCtx.contentResolver.openInputStream(uri)
}

@Throws(Exception::class)
fun DocumentFile.openOutputStream(): OutputStream? {
    return appCtx.contentResolver.openOutputStream(uri)
}

@Throws(Exception::class)
fun DocumentFile.writeText(context: Context, data: String, charset: Charset = Charsets.UTF_8) {
    uri.writeText(context, data, charset)
}

@Throws(Exception::class)
fun DocumentFile.writeBytes(context: Context, data: ByteArray) {
    uri.writeBytes(context, data)
}

@Throws(Exception::class)
fun DocumentFile.readText(context: Context): String {
    return String(readBytes(context))
}

@Throws(Exception::class)
fun DocumentFile.readBytes(context: Context): ByteArray {
    return context.contentResolver.openInputStream(uri)?.let {
        val len: Int = it.available()
        val buffer = ByteArray(len)
        it.read(buffer)
        it.close()
        return buffer
    } ?: throw RuntimeException("打开文件失败\n${uri}")
}

fun DocumentFile.checkWrite(): Boolean {
    return try {
        val filename = System.currentTimeMillis().toString()
        createFile(FileUtils.getMimeType(filename), filename)?.let {
            it.openOutputStream()?.let { out ->
                out.use { }
                it.delete()
                return true
            }
        }
        false
    } catch (e: Exception) {
        false
    }
}
