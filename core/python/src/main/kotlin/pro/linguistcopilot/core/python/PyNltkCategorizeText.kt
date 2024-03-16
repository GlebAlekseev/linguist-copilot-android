package pro.linguistcopilot.core.python

import com.chaquo.python.Python
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

// only en

class PyNltkCategorizeText {
    private val nltkCategorizeTextModule by lazy {
        Python.getInstance().getModule(NLTK_CATEGORIZE_TEXT_MODULE_NAME)
    }

    fun categorize(text: String, categoryKeywords: Map<String, List<String>>): List<String> {
        val categoryKeywordsJson = Json.encodeToString(categoryKeywords)
        val result = nltkCategorizeTextModule.callAttr(PROCESS_FUNCTION, text, categoryKeywordsJson)
        return result.asList().map { it.toString() }
    }

    companion object {
        private const val NLTK_CATEGORIZE_TEXT_MODULE_NAME = "nltk_categorize_text"
        private const val PROCESS_FUNCTION = "process"
    }
}