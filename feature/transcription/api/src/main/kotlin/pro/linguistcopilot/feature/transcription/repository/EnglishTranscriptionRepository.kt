package pro.linguistcopilot.feature.transcription.repository

interface EnglishTranscriptionRepository {
    fun getTranscription(word: String): String?
}