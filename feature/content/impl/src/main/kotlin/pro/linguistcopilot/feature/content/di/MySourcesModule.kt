package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.library.mySources.DefaultMySourcesComponent
import pro.linguistcopilot.feature.content.library.mySources.MySourcesComponent

@Module
interface MySourcesModule {

    @Binds
    fun componentFactory(impl: DefaultMySourcesComponent.Factory): MySourcesComponent.Factory
}
