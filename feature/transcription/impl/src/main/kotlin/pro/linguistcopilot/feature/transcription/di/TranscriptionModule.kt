package pro.linguistcopilot.feature.transcription.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.word.repository.EnglishTranscriptionRepository
import pro.linguistcopilot.feature.transcription.repository.EnglishTranscriptionRepositoryImpl


@Module
interface TranscriptionModule {
    @Binds
    fun englishTranscriptionRepository(impl: EnglishTranscriptionRepositoryImpl): EnglishTranscriptionRepository
}
