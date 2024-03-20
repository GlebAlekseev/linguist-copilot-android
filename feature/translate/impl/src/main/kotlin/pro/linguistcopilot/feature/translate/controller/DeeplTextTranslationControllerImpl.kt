package pro.linguistcopilot.feature.translate.controller

import pro.linguistcopilot.feature.translate.entity.TranslatedText
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import pro.linguistcopilot.feature.word.entity.Language
import javax.inject.Inject

class DeeplTextTranslationControllerImpl @Inject constructor() : TextTranslationController {
    override val supportedSourceLanguages: List<Language> =
        listOf(
            Language.Russian,
            Language.English,
        )
    override val supportedTargetLanguages: List<Language> =
        listOf(
            Language.Russian,
            Language.English,
        )

    override suspend fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        targetTranslationEngineConfig: TranslationEngineConfig
    ): TranslatedText? {
        return null
    }
}