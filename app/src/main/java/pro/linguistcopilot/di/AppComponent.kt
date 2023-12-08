package pro.linguistcopilot.di

import android.content.Context
import dagger.BindsInstance
import dagger.Component
import pro.linguistcopilot.App
import pro.linguistcopilot.MainActivity
import pro.linguistcopilot.core.utils.di.ApplicationContext
import pro.linguistcopilot.di.module.CoreModule
import pro.linguistcopilot.di.module.FeatureTogglesModule
import pro.linguistcopilot.di.module.ReaderDependenciesModule
import pro.linguistcopilot.di.module.SplashDependenciesModule
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.features.reader.di.ReaderDependencies
import pro.linguistcopilot.features.splash.di.SplashDependencies


@AppComponentScope
@Component(
    modules = [SplashDependenciesModule::class, ReaderDependenciesModule::class, FeatureTogglesModule::class, CoreModule::class]
)
interface AppComponent : SplashDependencies, ReaderDependencies {
    fun inject(application: App)
    fun inject(mainActivity: MainActivity)

    @Component.Factory
    interface Factory {
        fun create(
            @[BindsInstance ApplicationContext] context: Context
        ): AppComponent
    }
}