package pro.linguistcopilot.features.splash.di

import dagger.Component
import pro.linguistcopilot.features.splash.SplashFragment
import pro.linguistcopilot.features.splash.di.scope.SplashComponentScope

@SplashComponentScope
@Component(
    dependencies = [SplashDependencies::class],
    modules = []
)
interface SplashComponent {
    fun inject(splashFragment: SplashFragment)

    @Component.Factory
    interface Factory {
        fun create(dependencies: SplashDependencies): SplashComponent
    }
}