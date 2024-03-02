package pro.linguistcopilot.core.book

import android.net.Uri
import android.webkit.MimeTypeMap
import me.ag2s.epublib.util.zip.ZipException
import pro.linguistcopilot.core.book.exception.UnsupportedFormatException
import pro.linguistcopilot.core.utils.MD5Utils
import splitties.init.appCtx
import java.io.File
import java.io.FileOutputStream

object BookFactory {
    @Throws(UnsupportedFormatException::class, ZipException::class)
    fun getBookSource(uri: Uri): BookSource {
        val localUri = copyToLocalIfNotExist(uri)
        val path = localUri?.path
        return when {
            path != null && path.endsWith(".epub") -> EpubSource(localUri)
            else -> throw UnsupportedFormatException("Unsupported file format for: $localUri")
        }
    }

    private fun copyToLocalIfNotExist(uri: Uri): Uri? {
        try {
            val contextResolver = appCtx.contentResolver
            val inputStream1 = contextResolver.openInputStream(uri)!!
            val outputDir = File(appCtx.filesDir, "books")
            if (!outputDir.exists()) {
                outputDir.mkdir()
            }
            val md5Hash = MD5Utils.md5Encode(inputStream1)
            inputStream1.close()
            val inputStream = contextResolver.openInputStream(uri)!!
            val fileExtension = MimeTypeMap.getFileExtensionFromUrl(uri.toString())
            val outputFileName = "$md5Hash.$fileExtension"
            val outputFile = File(outputDir, outputFileName)
            if (outputFile.exists()) {
                return Uri.fromFile(outputFile)
            }
            FileOutputStream(outputFile).use { output ->
                val buffer = ByteArray(4 * 1024)
                var read: Int
                while (inputStream.read(buffer).also { read = it } != -1) {
                    output.write(buffer, 0, read)
                }
                output.flush()
            }
            return Uri.fromFile(outputFile)
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}