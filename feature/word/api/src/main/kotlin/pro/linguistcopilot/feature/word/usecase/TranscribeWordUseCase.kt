package pro.linguistcopilot.feature.word.usecase

import pro.linguistcopilot.core.utils.UseCase
import pro.linguistcopilot.feature.textProcessing.controller.TextProcessingController
import pro.linguistcopilot.feature.textProcessing.entity.Lang
import pro.linguistcopilot.feature.word.entity.WordTranscription
import pro.linguistcopilot.feature.word.repository.EnglishTranscriptionRepository
import javax.inject.Inject

class TranscribeWordUseCase @Inject constructor(
    private val textProcessingController: TextProcessingController,
    private val englishTranscriptionRepository: EnglishTranscriptionRepository
) : UseCase<TranscribeWordUseCase.Params, WordTranscription?>() {

    @JvmInline
    value class Params(val word: String) : UseCase.Params

    override suspend fun execute(params: Params): WordTranscription? {
        val lang = textProcessingController.getTextLanguage(params.word)
        return when (lang) {
            Lang.EN -> {
                return WordTranscription(
                    text = params.word,
                    transcription = englishTranscriptionRepository.getTranscription(params.word)
                )
            }

            Lang.UNKNOWN -> null
        }
    }
}