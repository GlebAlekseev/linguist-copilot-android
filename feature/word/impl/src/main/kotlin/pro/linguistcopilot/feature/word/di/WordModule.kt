package pro.linguistcopilot.feature.word.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.word.repository.EnglishTranscriptionRepository
import pro.linguistcopilot.feature.word.repository.EnglishTranscriptionRepositoryImpl


@Module
interface WordModule {
    @Binds
    fun englishTranscriptionRepository(impl: EnglishTranscriptionRepositoryImpl): EnglishTranscriptionRepository
}
