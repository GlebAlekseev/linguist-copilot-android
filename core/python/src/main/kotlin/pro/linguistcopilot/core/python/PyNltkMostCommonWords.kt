package pro.linguistcopilot.core.python

import com.chaquo.python.Python

// only en

class PyNltkMostCommonWords {
    private val nltkMostCommonWordsModule by lazy {
        Python.getInstance().getModule(NLTK_MOST_COMMON_WORDS_MODULE_NAME)
    }

    fun mostCommon(text: String, numberWords: Int): List<Pair<String, Int>> {
        val result = nltkMostCommonWordsModule.callAttr(PROCESS_FUNCTION, text, numberWords)
        return result.asList().filterNotNull().map {
            val tuple = it.asList()
            val word = tuple.getOrNull(0)?.toString() ?: ""
            val pos = tuple.getOrNull(1)?.toInt() ?: 0
            word to pos
        }
    }

    companion object {
        private const val NLTK_MOST_COMMON_WORDS_MODULE_NAME = "nltk_most_common_words"
        private const val PROCESS_FUNCTION = "process"
    }
}