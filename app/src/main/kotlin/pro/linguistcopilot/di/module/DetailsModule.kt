package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.details.DefaultDetailsComponent
import pro.linguistcopilot.details.DetailsComponent

@Module(includes = [])
interface DetailsModule {

    @Binds
    fun componentFactory(impl: DefaultDetailsComponent.Factory): DetailsComponent.Factory
}
