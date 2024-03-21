package pro.linguistcopilot.feature.translate.controller

import pro.linguistcopilot.feature.settings.repository.SettingsRepository
import pro.linguistcopilot.feature.translate.di.qualifier.DeeplFreeQualifier
import pro.linguistcopilot.feature.translate.di.qualifier.DeeplProQualifier
import pro.linguistcopilot.feature.translate.entity.TranslatedText
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import pro.linguistcopilot.feature.translate.retrofit.deepl.DeeplService
import pro.linguistcopilot.feature.word.entity.Language
import javax.inject.Inject

class DeeplTextTranslationControllerImpl @Inject constructor(
    @DeeplFreeQualifier private val deeplFreeService: DeeplService,
    @DeeplProQualifier private val deeplProService: DeeplService,
    private val settingsRepository: SettingsRepository
) : TextTranslationController {
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
        if (sourceLanguage !in supportedSourceLanguages) return null
        if (targetLanguage !in supportedTargetLanguages) return null
        try {
            val deeplService =
                if (settingsRepository.isFreeDeeplApi) deeplFreeService else deeplProService
            val result = deeplService.translate(
                query = text,
                source = mapLanguage(sourceLanguage),
                target = mapLanguage(targetLanguage)
            )
            return TranslatedText(
                text = text,
                translatedText = result.translations.firstOrNull()?.text ?: run { return null },
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                engineTitle = "Deepl ${if (settingsRepository.isFreeDeeplApi) "Free" else "Pro"}"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun mapLanguage(language: Language): String {
        return when (language) {
            Language.English -> "EN"
            Language.Russian -> "RU"
            else -> throw RuntimeException("Unknown language")
        }
    }
}