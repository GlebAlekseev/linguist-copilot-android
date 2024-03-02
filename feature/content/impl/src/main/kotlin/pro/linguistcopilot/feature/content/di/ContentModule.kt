package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.DefaultContentComponent
import pro.linguistcopilot.feature.content.ContentComponent

@Module
interface ContentModule {

    @Binds
    fun componentFactory(impl: DefaultContentComponent.Factory): ContentComponent.Factory
}
