package pro.linguistcopilot.core.python

import com.chaquo.python.Python
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

// only en

class PySpacyPassiveVoiceHandler {
    private val spacyPassiveVoiceHandlerModule by lazy {
        Python.getInstance().getModule(SPACY_PASSIVE_VOICE_HANDLER_MODULE_NAME)
    }

    fun handlePassiveVoice(text: String): List<SpacyPassiveVoiceResult> {
        val result = spacyPassiveVoiceHandlerModule.callAttr(PROCESS_FUNCTION, text)
        val jsonString = result.toString()
        return Json.decodeFromString<List<SpacyPassiveVoiceResult>>(jsonString)
    }

    companion object {
        private const val SPACY_PASSIVE_VOICE_HANDLER_MODULE_NAME = "spacy_passive_voice_handler"
        private const val PROCESS_FUNCTION = "process"
    }
}

@Serializable
data class SpacyPassiveVoiceResult(
    @SerialName("sentence") val sentence: String,
    @SerialName("valid") val isPassive: Boolean,
    @SerialName("formatted_sentence") val activeSentence: String?,
    @SerialName("base_verb") val baseVerb: String?
)