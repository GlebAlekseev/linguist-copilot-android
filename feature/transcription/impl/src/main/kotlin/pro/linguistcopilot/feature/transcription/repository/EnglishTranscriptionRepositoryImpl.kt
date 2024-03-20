package pro.linguistcopilot.feature.transcription.repository

import android.content.Context
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.feature.transcription.ArpaToIpa
import pro.linguistcopilot.feature.word.repository.EnglishTranscriptionRepository
import java.io.BufferedReader
import java.io.InputStreamReader
import javax.inject.Inject

class EnglishTranscriptionRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : EnglishTranscriptionRepository {
    private val transcriptionsMap = mutableMapOf<String, String>()

    init {
        loadTranscriptions()
    }

    private fun loadTranscriptions() {
        try {
            val inputStream = context.assets.open("cmudict-0.7b")
            val reader = BufferedReader(InputStreamReader(inputStream))
            var line: String?
            while (reader.readLine().also { line = it } != null) {
                val parts = line!!.split("\\s+".toRegex()).toTypedArray()
                if (parts.size >= 2) {
                    val word = parts[0]
                    val transcription = parts.slice(1 until parts.size).joinToString(" ")
                    transcriptionsMap[word] = transcription
                }
            }
            inputStream.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun getTranscription(word: String): String? {
        return transcriptionsMap[word]?.let { ArpaToIpa.arpaToIpa(it) }
    }
}