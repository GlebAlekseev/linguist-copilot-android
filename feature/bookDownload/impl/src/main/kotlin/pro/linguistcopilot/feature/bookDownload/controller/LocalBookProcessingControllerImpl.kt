package pro.linguistcopilot.feature.bookDownload.controller

import android.content.Context
import pro.linguistcopilot.core.converter.LocalConverterUtils
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.core.file.openInputStreamByPath
import pro.linguistcopilot.feature.book.controller.LocalBookFileController
import pro.linguistcopilot.feature.book.entity.MetaInfo
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.security.MessageDigest
import java.util.UUID
import javax.inject.Inject

class LocalBookProcessingControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context,
    private val localBookFileController: LocalBookFileController
) : LocalBookProcessingController {
    override fun getHash(uri: String): String {
        val inputStream = context.openInputStreamByPath(uri)
        val hash = calculateHash(inputStream)
        inputStream.close()
        return hash
    }

    private fun calculateHash(fileInputStream: FileInputStream): String {
        val md = MessageDigest.getInstance("SHA-256")

        val buffer = ByteArray(8192)
        var bytesRead = fileInputStream.read(buffer)

        while (bytesRead != -1) {
            md.update(buffer, 0, bytesRead)
            bytesRead = fileInputStream.read(buffer)
        }

        val digest = md.digest()
        return bytesToHex(digest)
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val hexChars = "0123456789ABCDEF".toCharArray()
        val result = StringBuilder(bytes.size * 2)
        for (byte in bytes) {
            val i = byte.toInt() and 0xFF
            result.append(hexChars[i shr 4 and 0x0F])
            result.append(hexChars[i and 0x0F])
        }
        return result.toString()
    }

    override fun isValidExt(uri: String): Boolean = true

    override fun isValidSize(uri: String): Boolean = true

    override fun isValidMimeType(uri: String): Boolean = true

    override fun convert2epub(uri: String): String? {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            return if (uri.endsWith(".epub")) localBookFileController.copyToLocal(uri) else {
                val fileName = UUID.randomUUID().toString() + ".epub"
                val directoryPath = LocalBookFileController.LOCAL_FILE_STORAGE_PATH
                val filePath =
                    context.filesDir.absolutePath + File.separator + directoryPath + File.separator + fileName
                val directory = File(directoryPath)
                directory.mkdirs()
                fileInputStream = context.openInputStreamByPath(uri)
                fileOutputStream = FileOutputStream(filePath)
                LocalConverterUtils.convertPdfToEpub(
                    inputStream = fileInputStream,
                    outputStream = fileOutputStream
                )
                fileOutputStream.close()
                fileInputStream.close()
                filePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fileOutputStream?.close()
            fileInputStream?.close()
            return null
        } finally {
            fileInputStream?.close()
        }
    }

    override fun convert2pdf(uri: String): String? {
        var fileInputStream: FileInputStream? = null
        var fileOutputStream: FileOutputStream? = null
        try {
            return if (uri.endsWith(".pdf")) localBookFileController.copyToLocal(uri) else {
                val fileName = UUID.randomUUID().toString() + ".pdf"
                val directoryPath = LocalBookFileController.LOCAL_FILE_STORAGE_PATH
                val filePath =
                    context.filesDir.absolutePath + File.separator + directoryPath + File.separator + fileName
                val directory = File(directoryPath)
                directory.mkdirs()
                fileInputStream = context.openInputStreamByPath(uri)
                fileOutputStream = FileOutputStream(filePath)
                LocalConverterUtils.convertEpubToPdf(
                    context = context,
                    inputStream = fileInputStream,
                    outputStream = fileOutputStream
                )
                filePath
            }
        } catch (e: Exception) {
            e.printStackTrace()
            fileOutputStream?.close()
            fileInputStream?.close()
            return null
        } finally {
            fileInputStream?.close()
        }
    }

    override fun getMetaInfo(pdfUri: String?, epubUri: String?): MetaInfo {
        return MetaInfo(
            description = "Описание"
        )
    }
}