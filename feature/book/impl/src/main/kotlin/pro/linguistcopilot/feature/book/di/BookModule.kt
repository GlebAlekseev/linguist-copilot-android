package pro.linguistcopilot.feature.book.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.book.BookItemRepositoryImpl
import pro.linguistcopilot.feature.book.repository.BookItemRepository


@Module
interface BookModule {
    @Binds
    fun bookItemRepository(impl: BookItemRepositoryImpl): BookItemRepository
}
