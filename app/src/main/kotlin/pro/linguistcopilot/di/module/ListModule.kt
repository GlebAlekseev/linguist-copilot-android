package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.list.DefaultListComponent
import pro.linguistcopilot.list.ListComponent

@Module(includes = [])
interface ListModule {

    @Binds
    fun componentFactory(impl: DefaultListComponent.Factory): ListComponent.Factory
}
