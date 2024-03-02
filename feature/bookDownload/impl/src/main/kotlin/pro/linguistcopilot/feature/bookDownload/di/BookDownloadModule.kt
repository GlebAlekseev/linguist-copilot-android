package pro.linguistcopilot.feature.bookDownload.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookDownload.BookDownloadComponent
import pro.linguistcopilot.feature.bookDownload.DefaultBookDownloadComponent

@Module
interface BookDownloadModule {

    @Binds
    fun componentFactory(impl: DefaultBookDownloadComponent.Factory): BookDownloadComponent.Factory
}
