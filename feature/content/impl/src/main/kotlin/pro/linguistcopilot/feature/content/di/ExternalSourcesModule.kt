package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.library.externalSources.DefaultExternalSourcesComponent
import pro.linguistcopilot.feature.content.library.externalSources.ExternalSourcesComponent

@Module
interface ExternalSourcesModule {

    @Binds
    fun componentFactory(impl: DefaultExternalSourcesComponent.Factory): ExternalSourcesComponent.Factory
}
