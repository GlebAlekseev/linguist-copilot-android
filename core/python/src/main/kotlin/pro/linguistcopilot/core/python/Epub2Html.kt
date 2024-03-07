package pro.linguistcopilot.core.python

import com.chaquo.python.Python

class Epub2Html {
    private val epub2htmlModule by lazy {
        Python.getInstance().getModule(EPUB2HTML_MODULE_NAME)
    }

    /**
     * Converts an EPUB file represented as a byte array into a ZIP file containing HTML and its dependencies,
     * or returns an error if conversion fails.
     * @param byteArray Byte array representing the EPUB file.
     * @return Byte array representing the ZIP file containing HTML and its dependencies, or an error.
     */
    fun convert(byteArray: ByteArray): ByteArray {
        val result = epub2htmlModule.callAttr(PROCESS_FUNCTION, byteArray)
        return result.toJava(ByteArray::class.java)
    }

    companion object {
        private const val EPUB2HTML_MODULE_NAME = "epub2html"
        private const val PROCESS_FUNCTION = "process"
    }
}