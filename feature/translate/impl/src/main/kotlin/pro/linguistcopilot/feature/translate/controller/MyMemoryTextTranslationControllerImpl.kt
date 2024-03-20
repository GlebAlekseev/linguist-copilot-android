package pro.linguistcopilot.feature.translate.controller

import pro.linguistcopilot.feature.translate.entity.TranslatedText
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import pro.linguistcopilot.feature.translate.retrofit.mymemory.MyMemoryService
import pro.linguistcopilot.feature.word.entity.Language
import javax.inject.Inject

class MyMemoryTextTranslationControllerImpl @Inject constructor(
    private val myMemoryService: MyMemoryService
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
            val result = myMemoryService.translate(
                query = text,
                langPair = "${mapLanguage(sourceLanguage)}|${mapLanguage(targetLanguage)}",
                de = null
            )
            if (result.responseStatus != 200) return null
            return TranslatedText(
                text = text,
                translatedText = result.responseData!!.translatedText,
                sourceLanguage = sourceLanguage,
                targetLanguage = targetLanguage,
                engineTitle = "MyMemory"
            )
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun mapLanguage(language: Language): String {
        return when (language) {
            Language.English -> "en"
            Language.Russian -> "ru"
            else -> throw RuntimeException("Unknown language")
        }
    }
}