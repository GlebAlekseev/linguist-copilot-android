package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap
import pro.linguistcopilot.core.utils.di.Dependencies
import pro.linguistcopilot.core.utils.di.DependenciesKey
import pro.linguistcopilot.di.AppComponent
import pro.linguistcopilot.features.splash.di.SplashDependencies

@Module
interface SplashDependenciesModule {
    @Binds
    @IntoMap
    @DependenciesKey(SplashDependencies::class)
    fun bindAuthDependencies(impl: AppComponent): Dependencies
}