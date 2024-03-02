package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.onboarding.di.OnboardingModule
import pro.linguistcopilot.root.DefaultRootComponent
import pro.linguistcopilot.root.RootComponent

@Module(includes = [OnboardingModule::class])
interface RootModule {

    @Binds
    fun componentFactory(impl: DefaultRootComponent.Factory): RootComponent.Factory
}
