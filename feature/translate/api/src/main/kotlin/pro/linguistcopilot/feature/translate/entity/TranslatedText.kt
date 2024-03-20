package pro.linguistcopilot.feature.translate.entity

import pro.linguistcopilot.feature.word.entity.Language

data class TranslatedText(
    val text: String,
    val translatedText: String,
    val sourceLanguage: Language,
    val targetLanguage: Language,
    val engineTitle: String
)