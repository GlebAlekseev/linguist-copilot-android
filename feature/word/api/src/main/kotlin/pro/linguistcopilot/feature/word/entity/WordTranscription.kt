package pro.linguistcopilot.feature.word.entity

data class WordTranscription(
    val text: String,
    val transcription: String?,
    val language: Language
)