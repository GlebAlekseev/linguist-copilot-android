package pro.linguistcopilot.feature.content.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.content.library.DefaultLibraryComponent
import pro.linguistcopilot.feature.content.library.LibraryComponent

@Module(includes = [ExternalSourcesModule::class, MySourcesModule::class])
interface LibraryModule {

    @Binds
    fun componentFactory(impl: DefaultLibraryComponent.Factory): LibraryComponent.Factory
}
