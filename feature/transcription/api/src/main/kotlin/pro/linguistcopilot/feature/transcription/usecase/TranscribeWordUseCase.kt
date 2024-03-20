package pro.linguistcopilot.feature.transcription.usecase

import pro.linguistcopilot.core.utils.UseCase
import pro.linguistcopilot.feature.textProcessing.controller.TextProcessingController
import pro.linguistcopilot.feature.transcription.entity.WordTranscription
import pro.linguistcopilot.feature.transcription.repository.EnglishTranscriptionRepository
import pro.linguistcopilot.feature.word.entity.Language
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
            Language.English -> {
                return WordTranscription(
                    text = params.word,
                    transcription = englishTranscriptionRepository.getTranscription(params.word)
                )
            }

            else -> null
        }
    }
}