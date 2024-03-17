package pro.linguistcopilot.core.python

import com.chaquo.python.Python
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// only en

class PySpacyKeywordExtraction {
    private val spacyKeywordExtractionModule by lazy {
        Python.getInstance().getModule(SPACY_KEYWORD_EXTRACTION_MODULE_NAME)
    }

    fun extractKeywords(text: String, numberWords: Int): List<SpacyKeywordExtractionResult> {
        val result = spacyKeywordExtractionModule.callAttr(PROCESS_FUNCTION, text, numberWords)
        val jsonString = result.toString()
        return Json.decodeFromString<List<SpacyKeywordExtractionResult>>(jsonString)
    }

    companion object {
        private const val SPACY_KEYWORD_EXTRACTION_MODULE_NAME = "spacy_keyword_extraction"
        private const val PROCESS_FUNCTION = "process"
    }
}

@Serializable
data class SpacyKeywordExtractionResult(
    @SerialName("keyword") val keyword: String,
    @SerialName("score") val score: Float,
)