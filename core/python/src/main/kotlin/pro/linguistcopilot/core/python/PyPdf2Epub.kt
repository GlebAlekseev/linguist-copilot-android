package pro.linguistcopilot.core.python

import com.chaquo.python.Python

class PyPdf2Epub {
    private val pdf2epubModule by lazy {
        Python.getInstance().getModule(PDF2EPUB_MODULE_NAME)
    }

    /**
     * Converts a PDF file represented as a byte array into a ZIP file containing three files:
     * TXT, HTML, and EPUB, or returns an error if conversion fails.
     * @param byteArray Byte array representing the PDF file.
     * @return Byte array representing the ZIP file containing TXT, HTML, and EPUB files, or an error.
     */
    fun convert(byteArray: ByteArray): ByteArray {
        val result = pdf2epubModule.callAttr(PROCESS_FUNCTION, byteArray)
        return result.toJava(ByteArray::class.java)
    }

    companion object {
        private const val PDF2EPUB_MODULE_NAME = "pdf2epub"
        private const val PROCESS_FUNCTION = "process"
    }
}