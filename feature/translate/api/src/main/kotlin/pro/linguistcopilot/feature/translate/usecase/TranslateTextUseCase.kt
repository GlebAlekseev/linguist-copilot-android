package pro.linguistcopilot.feature.translate.usecase

import pro.linguistcopilot.core.utils.UseCase
import pro.linguistcopilot.feature.textProcessing.controller.TextProcessingController
import pro.linguistcopilot.feature.translate.controller.TextTranslationController
import pro.linguistcopilot.feature.translate.di.DeeplQualifier
import pro.linguistcopilot.feature.translate.di.MlKitQualifier
import pro.linguistcopilot.feature.translate.di.MyMemoryQualifier
import pro.linguistcopilot.feature.translate.entity.TranslatedText
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import pro.linguistcopilot.feature.translate.repository.TranslationEngineConfigRepository
import pro.linguistcopilot.feature.word.entity.Language
import javax.inject.Inject


class TranslateTextUseCase @Inject constructor(
    @MlKitQualifier private val mlKitTextTranslationController: TextTranslationController,
    @DeeplQualifier private val deeplTextTranslationController: TextTranslationController,
    @MyMemoryQualifier private val myMemoryTextTranslationController: TextTranslationController,
    private val translationEngineConfigRepository: TranslationEngineConfigRepository,
    private val textProcessingController: TextProcessingController
) : UseCase<TranslateTextUseCase.Params, TranslatedText?>() {

    data class Params(
        val text: String,
        val sourceLanguage: Language,
        val targetLanguage: Language,
        var targetTranslationEngineConfig: TranslationEngineConfig?
    ) : UseCase.Params

    override suspend fun execute(params: Params): TranslatedText? {
        val sourceLanguage =
            if (params.sourceLanguage is Language.Auto) textProcessingController.getTextLanguage(
                params.text
            )
            else params.sourceLanguage
        if (params.targetLanguage is Language.Auto) return null

        params.targetTranslationEngineConfig?.let { config ->
            return handleTranslationEngineConfig(
                params.text,
                sourceLanguage,
                params.targetLanguage,
                config
            )
        }

        var result: TranslatedText? = null
        for (config in translationEngineConfigRepository.engineConfigs) {
            result = handleTranslationEngineConfig(
                params.text,
                sourceLanguage,
                params.targetLanguage,
                config
            )
            if (result != null) break
        }
        return result
    }

    private suspend fun handleTranslationEngineConfig(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        targetTranslationEngineConfig: TranslationEngineConfig
    ): TranslatedText? {
        return when (val config = targetTranslationEngineConfig) {
            is TranslationEngineConfig.Deepl -> {
                deeplTextTranslationController.translate(
                    text = text,
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    targetTranslationEngineConfig = config
                )
            }

            is TranslationEngineConfig.MlKit -> {
                mlKitTextTranslationController.translate(
                    text = text,
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    targetTranslationEngineConfig = config
                )
            }

            is TranslationEngineConfig.MyMemory -> {
                myMemoryTextTranslationController.translate(
                    text = text,
                    sourceLanguage = sourceLanguage,
                    targetLanguage = targetLanguage,
                    targetTranslationEngineConfig = config
                )
            }
        }
    }
}