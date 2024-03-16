package pro.linguistcopilot.core.python

import com.chaquo.python.Python

// only en

class PyNltkKeywordExtraction {
    private val nltkKeywordExtractionModule by lazy {
        Python.getInstance().getModule(NLTK_KEYWORD_EXTRACTION_MODULE_NAME)
    }

    fun extractKeywords(text: String, numberWords: Int): List<Pair<String, Float>> {
        val result = nltkKeywordExtractionModule.callAttr(PROCESS_FUNCTION, text, numberWords)
        return result.asList().filterNotNull().map {
            val tuple = it.asList()
            val word = tuple.getOrNull(0)?.toString() ?: ""
            val pos = tuple.getOrNull(1)?.toFloat() ?: 0f
            word to pos
        }
    }

    companion object {
        private const val NLTK_KEYWORD_EXTRACTION_MODULE_NAME = "nltk_keyword_extraction"
        private const val PROCESS_FUNCTION = "process"
    }
}