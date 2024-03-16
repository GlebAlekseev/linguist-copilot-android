package pro.linguistcopilot.core.python

import com.chaquo.python.Python

// only en

class PyNltkPOSForText {
    private val nltkPOSForTextModule by lazy {
        Python.getInstance().getModule(NLTK_POS_FOR_TEXT_MODULE_NAME)
    }

    fun getPOSForText(text: String) : List<Pair<String, String>> {
        val result = nltkPOSForTextModule.callAttr(PROCESS_FUNCTION, text)
        return result.asList().filterNotNull().map {
            val tuple = it.asList()
            val word = tuple.getOrNull(0)?.toString() ?: ""
            val pos = tuple.getOrNull(1)?.toString() ?: ""
            word to pos
        }
    }

    companion object {
        private const val NLTK_POS_FOR_TEXT_MODULE_NAME = "nltk_pos_for_text"
        private const val PROCESS_FUNCTION = "process"
    }
}