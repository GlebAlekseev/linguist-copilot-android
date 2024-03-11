package pro.linguistcopilot.core.converter

import android.content.Context
import pro.linguistcopilot.core.python.PyEpub2Html
import pro.linguistcopilot.core.python.PyHtmlMerge
import pro.linguistcopilot.core.python.PyPdf2Epub
import java.io.File
import java.io.FileInputStream
import java.io.InputStream
import java.io.OutputStream

object LocalConverterUtils {
    fun convertPdfToEpub(inputStream: InputStream, outputStream: OutputStream) {
        val result = PyPdf2Epub().convert(inputStream.readBytes())
        outputStream.write(result)
        outputStream.flush()
    }

    fun convertEpubToPdf(context: Context, inputStream: InputStream, outputStream: OutputStream) {
        val tempFile = File.createTempFile("tempPdf", ".pdf")

        val htmlZipByteArray = PyEpub2Html().convert(inputStream.readBytes())
        val mergedHtml = PyHtmlMerge().merge(htmlZipByteArray)
        val htmlString = mergedHtml.toString(Charsets.UTF_8)
        Html2Pdf.Companion.Builder()
            .context(context)
            .html(htmlString)
            .file(tempFile)
            .build()
            .also {
                it.convertToPdf(
                    object : Html2Pdf.OnCompleteConversion {
                        override fun onSuccess() {
                            val tempFileInputStream = FileInputStream(tempFile)
                            val tempFileBytes = tempFileInputStream.readBytes()
                            outputStream.write(tempFileBytes)
                            tempFile.delete()
                            tempFileInputStream.close()
                        }

                        override fun onFailed() {}
                    }
                )
            }
    }
}