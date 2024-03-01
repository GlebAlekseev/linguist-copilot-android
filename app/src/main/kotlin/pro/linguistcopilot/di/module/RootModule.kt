package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.root.DefaultRootComponent
import pro.linguistcopilot.root.RootComponent

@Module(includes = [ListModule::class, DetailsModule::class])
interface RootModule {

    @Binds
    fun componentFactory(impl: DefaultRootComponent.Factory): RootComponent.Factory
}
