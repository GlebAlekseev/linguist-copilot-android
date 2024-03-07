package pro.linguistcopilot.core.python

import com.chaquo.python.Python

class PyHtmlMerge {
    private val htmlMergeModule by lazy {
        Python.getInstance().getModule(HTML_MERGE_MODULE_NAME)
    }

    /**
     * Merges the dependencies of a ZIP file represented as a byte array into a single HTML file
     * and returns the byte stream of the merged HTML file, or an error if merging fails.
     * @param byteArray Byte array representing the ZIP file containing dependencies.
     * @return Byte array representing the merged HTML file, or an error.
     */
    fun merge(byteArray: ByteArray) : ByteArray {
        val result = htmlMergeModule.callAttr(PROCESS_FUNCTION, byteArray)
        return result.toJava(ByteArray::class.java)
    }

    companion object {
        private const val HTML_MERGE_MODULE_NAME = "htmlmerge"
        private const val PROCESS_FUNCTION = "process"
    }
}