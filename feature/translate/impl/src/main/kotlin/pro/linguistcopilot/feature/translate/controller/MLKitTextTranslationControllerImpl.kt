package pro.linguistcopilot.feature.translate.controller

import android.util.LruCache
import com.google.android.gms.tasks.Tasks
import com.google.mlkit.nl.translate.TranslateLanguage
import com.google.mlkit.nl.translate.Translation
import com.google.mlkit.nl.translate.Translator
import com.google.mlkit.nl.translate.TranslatorOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import pro.linguistcopilot.feature.translate.entity.Language
import javax.inject.Inject

class MLKitTextTranslationControllerImpl @Inject constructor() :
    TextTranslationController, AutoCloseable {
    override val availableLanguages: List<Language> =
        listOf(
            Language.Russian(code = TranslateLanguage.RUSSIAN),
            Language.English(code = TranslateLanguage.ENGLISH),
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
        targetLanguage: Language
    ): String {
        val translatorOptions = TranslatorOptions.Builder()
            .setSourceLanguage(sourceLanguage.code)
            .setTargetLanguage(targetLanguage.code)
            .build()
        val translator = translators.get(translatorOptions)

        val translationTask = translator.downloadModelIfNeeded().continueWith {
            translator.translate(text)
        }

        val translationResult = withContext(Dispatchers.IO) {
            Tasks.await(translationTask)
        }

        return Tasks.await(translationResult)
    }

    override fun close() {
        translators.evictAll()
    }

    companion object {
        private const val NUM_TRANSLATORS = 3
    }
}