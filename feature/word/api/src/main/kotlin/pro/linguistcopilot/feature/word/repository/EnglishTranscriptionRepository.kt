package pro.linguistcopilot.feature.word.repository

interface EnglishTranscriptionRepository {
    fun getTranscription(word: String): String?
}