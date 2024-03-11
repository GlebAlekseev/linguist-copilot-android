package pro.linguistcopilot.feature.bookDownload.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookDownload.BookDownloadComponent
import pro.linguistcopilot.feature.bookDownload.DefaultBookDownloadComponent
import pro.linguistcopilot.feature.bookDownload.controller.LoadBookServiceController
import pro.linguistcopilot.feature.bookDownload.controller.LoadBookServiceControllerImpl
import pro.linguistcopilot.feature.bookDownload.controller.LocalBookProcessingController
import pro.linguistcopilot.feature.bookDownload.controller.LocalBookProcessingControllerImpl

@Module
interface BookDownloadModule {

    @Binds
    fun componentFactory(impl: DefaultBookDownloadComponent.Factory): BookDownloadComponent.Factory

    @Binds
    fun loadBookServiceController(impl: LoadBookServiceControllerImpl): LoadBookServiceController

    @Binds
    fun localBookProcessingController(impl: LocalBookProcessingControllerImpl): LocalBookProcessingController
}
