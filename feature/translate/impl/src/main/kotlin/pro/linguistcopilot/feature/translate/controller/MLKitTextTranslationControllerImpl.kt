package pro.linguistcopilot.feature.translate.controller

import android.util.LruCache
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.linguistcopilot.feature.translate.entity.TranslatedText
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import pro.linguistcopilot.feature.word.entity.Language
import javax.inject.Inject

class MLKitTextTranslationControllerImpl @Inject constructor() : TextTranslationController {
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

    private val translators =
        object : LruCache<TranslatorOptions, Translator>(NUM_TRANSLATORS) {
            override fun create(options: TranslatorOptions): Translator {
                return Translation.getClient(options)
            }

            override fun entryRemoved(
                evicted: Boolean,
                key: TranslatorOptions,
                oldValue: Translator,
                newValue: Translator?,
            ) {
                oldValue.close()
            }
        }

    override suspend fun translate(
        text: String,
        sourceLanguage: Language,
        targetLanguage: Language,
        targetTranslationEngineConfig: TranslationEngineConfig
    ): TranslatedText? {
        if (sourceLanguage !in supportedSourceLanguages) return null
        if (targetLanguage !in supportedTargetLanguages) return null
        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(mapLanguage(sourceLanguage))
            .setTargetLanguage(mapLanguage(targetLanguage))
            .build()
        val translator = translators.get(translatorOptions)

        val translationTask = translator.downloadModelIfNeeded().continueWith {
            translator.translate(text)
        }

        val translationResult = withContext(Dispatchers.IO) {
            Tasks.await(translationTask)
        }

        return TranslatedText(
            text = text,
            translatedText = Tasks.await(translationResult),
            sourceLanguage = sourceLanguage,
            targetLanguage = targetLanguage,
            engineTitle = "MlKit"
        )
    }

    private fun mapLanguage(language: Language): String {
        return when (language) {
            Language.English -> TranslateLanguage.ENGLISH
            Language.Russian -> TranslateLanguage.RUSSIAN
            else -> throw RuntimeException("Unknown language")
        }
    }

    companion object {
        private const val NUM_TRANSLATORS = 3
    }
}