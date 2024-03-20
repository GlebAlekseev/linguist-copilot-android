package pro.linguistcopilot.feature.translate.controller

import pro.linguistcopilot.feature.translate.entity.TranslatedText
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import pro.linguistcopilot.feature.word.entity.Language

interface TextTranslationController {
    val supportedSourceLanguages: List<Language>
    val supportedTargetLanguages: List<Language>
    suspend fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        targetTranslationEngineConfig: TranslationEngineConfig
    ): TranslatedText?
}