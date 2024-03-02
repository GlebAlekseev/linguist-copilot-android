package pro.linguistcopilot.feature.bookReader.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookReader.BookReaderComponent
import pro.linguistcopilot.feature.bookReader.DefaultBookReaderComponent

@Module
interface BookReaderModule {

    @Binds
    fun componentFactory(impl: DefaultBookReaderComponent.Factory): BookReaderComponent.Factory
}
