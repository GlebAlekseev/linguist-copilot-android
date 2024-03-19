package pro.linguistcopilot.feature.translate.controller

import pro.linguistcopilot.feature.translate.entity.Language

interface TextTranslationController {
    val availableLanguages: List<Language>
    suspend fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language
    ): String
}