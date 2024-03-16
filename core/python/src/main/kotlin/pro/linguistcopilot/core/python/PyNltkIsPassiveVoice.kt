package pro.linguistcopilot.core.python

import com.chaquo.python.Python

// only en

class PyNltkIsPassiveVoice {
    private val nltkIsPassiveVoiceModule by lazy {
        Python.getInstance().getModule(NLTK_IS_PASSIVE_VOICE_MODULE_NAME)
    }

    fun isPassiveVoice(sentence: String): Boolean {
        val result = nltkIsPassiveVoiceModule.callAttr(PROCESS_FUNCTION, sentence)
        return result.toBoolean()
    }

    companion object {
        private const val NLTK_IS_PASSIVE_VOICE_MODULE_NAME = "nltk_is_passive_voice"
        private const val PROCESS_FUNCTION = "process"
    }
}