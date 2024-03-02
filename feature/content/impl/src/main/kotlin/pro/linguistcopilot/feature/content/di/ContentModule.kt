package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.ContentComponent
import pro.linguistcopilot.feature.content.DefaultContentComponent

@Module(includes = [LibraryModule::class, OverviewModule::class, ProfileModule::class])
interface ContentModule {

    @Binds
    fun componentFactory(impl: DefaultContentComponent.Factory): ContentComponent.Factory
}
