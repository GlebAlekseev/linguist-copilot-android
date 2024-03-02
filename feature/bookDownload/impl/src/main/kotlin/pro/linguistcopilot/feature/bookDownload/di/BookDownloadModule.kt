package pro.linguistcopilot.feature.bookDownload.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.auth.BookDownloadComponent
import pro.linguistcopilot.feature.auth.DefaultBookDownloadComponent

@Module
interface BookDownloadModule {

    @Binds
    fun componentFactory(impl: DefaultBookDownloadComponent.Factory): BookDownloadComponent.Factory
}
