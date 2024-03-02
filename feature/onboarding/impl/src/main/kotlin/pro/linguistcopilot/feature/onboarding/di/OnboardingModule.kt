package pro.linguistcopilot.feature.onboarding.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.onboarding.DefaultOnboardingComponent
import pro.linguistcopilot.feature.onboarding.OnboardingComponent

@Module(includes = [OnboardingPageModule::class])
interface OnboardingModule {

    @Binds
    fun componentFactory(impl: DefaultOnboardingComponent.Factory): OnboardingComponent.Factory
}
