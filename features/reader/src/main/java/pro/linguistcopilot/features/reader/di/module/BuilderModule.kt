package pro.linguistcopilot.features.reader.di.module

import dagger.Module
import dagger.Provides
import pro.linguistcopilot.core.utils.di.Dependencies
import pro.linguistcopilot.features.reader.di.ReaderDependencies
import pro.linguistcopilot.features.reader.domain.BookUrlArg

@Module
class BuilderModule(
    private val bookUrlArg: BookUrlArg,
    private val readerDependencies: ReaderDependencies
) {
    @Provides
    fun provideBookUrlArg(): BookUrlArg = bookUrlArg

    @Provides
    fun provideReaderDependencies(): Dependencies = readerDependencies
}