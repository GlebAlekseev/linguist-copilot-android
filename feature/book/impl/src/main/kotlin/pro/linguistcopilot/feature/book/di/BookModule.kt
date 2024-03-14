package pro.linguistcopilot.feature.book.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.book.repository.BookItemRepositoryImpl
import pro.linguistcopilot.feature.book.controller.LocalBookFileController
import pro.linguistcopilot.feature.book.controller.LocalBookFileControllerImpl
import pro.linguistcopilot.feature.book.repository.BookItemRepository


@Module
interface BookModule {
    @Binds
    fun bookItemRepository(impl: BookItemRepositoryImpl): BookItemRepository


    @Binds
    fun localFileController(impl: LocalBookFileControllerImpl): LocalBookFileController
}
