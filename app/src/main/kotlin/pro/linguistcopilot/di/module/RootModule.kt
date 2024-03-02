package pro.linguistcopilot.di.module

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.auth.di.AuthModule
import pro.linguistcopilot.feature.content.di.ContentModule
import pro.linguistcopilot.feature.onboarding.di.OnboardingModule
import pro.linguistcopilot.root.DefaultRootComponent
import pro.linguistcopilot.root.RootComponent

@Module(includes = [OnboardingModule::class, AuthModule::class, ContentModule::class])
interface RootModule {

    @Binds
    fun componentFactory(impl: DefaultRootComponent.Factory): RootComponent.Factory
}
