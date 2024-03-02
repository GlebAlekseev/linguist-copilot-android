package pro.linguistcopilot.feature.onboarding.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.onboarding.page.DefaultOnboardingPageComponent
import pro.linguistcopilot.feature.onboarding.page.OnboardingPageComponent

@Module
interface OnboardingPageModule {
    @Binds
    fun componentFactory(impl: DefaultOnboardingPageComponent.Factory): OnboardingPageComponent.Factory
}
