package pro.linguistcopilot.core.python

import com.chaquo.python.Python
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// only en

class PySpacySentenceParsing {
    private val spacyPassiveVoiceHandlerModule by lazy {
        Python.getInstance().getModule(SPACY_SENTENCE_PARSING_MODULE_NAME)
    }

    fun parseSentence(sentence: String): SpacySentenceParsingNodeResult {
        val result = spacyPassiveVoiceHandlerModule.callAttr(PROCESS_FUNCTION, sentence)
        val jsonString = result.toString()
        return Json.decodeFromString<SpacySentenceParsingNodeResult>(jsonString)
    }

    companion object {
        private const val SPACY_SENTENCE_PARSING_MODULE_NAME = "spacy_sentence_parsing"
        private const val PROCESS_FUNCTION = "process"
    }
}

@Serializable
data class SpacySentenceParsingNodeResult(
    @SerialName("token") val token: String,
    @SerialName("pos") val pos: String,
    @SerialName("rel") val rel: String,
    @SerialName("token_x_start") val tokenXStart: Int,
    @SerialName("token_x_end") val tokenXEnd: Int,
    @SerialName("group_x_start") val groupXStart: Int,
    @SerialName("group_x_end") val groupXEnd: Int,
    @SerialName("children") val children: List<SpacySentenceParsingNodeResult>,
)