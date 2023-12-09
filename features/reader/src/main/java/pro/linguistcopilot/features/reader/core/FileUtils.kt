package pro.linguistcopilot.features.reader.core

import android.content.Context
import android.net.Uri
import android.os.ParcelFileDescriptor
import splitties.init.appCtx
import java.io.File


object FileUtils {
    fun getPath(root: File, vararg subDirFiles: String): String {
        val path = StringBuilder(root.absolutePath)
        subDirFiles.forEach {
            if (it.isNotEmpty()) {
                path.append(File.separator).append(it)
            }
        }
        return path.toString()
    }

    fun getParcelFileDescriptor(uri: Uri): ParcelFileDescriptor? {
        return if (uri.isContentScheme()) {
            appCtx.contentResolver.openFileDescriptor(uri, "r")
        } else {
            ParcelFileDescriptor.open(File(uri.path!!), ParcelFileDescriptor.MODE_READ_ONLY)
        }
    }

    fun getFileName(path: String): String {
        val fileName = path.split("/").last()
        return String(fileName.toByteArray(), Charsets.UTF_8)
    }

    fun createFileIfNotExist(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            file.parent?.let {
                createFolderIfNotExist(it)
            }
            file.createNewFile()
        }
        return file
    }

    private fun createFolderIfNotExist(filePath: String): File {
        val file = File(filePath)
        if (!file.exists()) {
            file.mkdirs()
        }
        return file
    }
}

val Context.externalFiles: File
    get() = this.getExternalFilesDir(null) ?: this.filesDir

