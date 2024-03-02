package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.library.external_sources.DefaultExternalSourcesComponent
import pro.linguistcopilot.feature.content.library.external_sources.ExternalSourcesComponent

@Module
interface ExternalSourcesModule {

    @Binds
    fun componentFactory(impl: DefaultExternalSourcesComponent.Factory): ExternalSourcesComponent.Factory
}
