package pro.linguistcopilot.core.python

import com.chaquo.python.Python

// only en

class PyNltkSummarize {
    private val nltkSummarizeModule by lazy {
        Python.getInstance().getModule(NLTK_SUMMARIZE_MODULE_NAME)
    }

    fun summarize(text: String, numberSentences: Int): String {
        val result = nltkSummarizeModule.callAttr(PROCESS_FUNCTION, text, numberSentences)
        return result.toString()
    }

    companion object {
        private const val NLTK_SUMMARIZE_MODULE_NAME = "nltk_summarize"
        private const val PROCESS_FUNCTION = "process"
    }
}