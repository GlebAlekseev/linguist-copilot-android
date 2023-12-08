package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pro.linguistcopilot.core.utils.di.Dependencies
import pro.linguistcopilot.core.utils.di.DependenciesKey
import pro.linguistcopilot.di.AppComponent
import pro.linguistcopilot.features.reader.di.ReaderDependencies

@Module
interface ReaderDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(ReaderDependencies::class)
    fun bindReaderDependencies(impl: AppComponent): Dependencies
}