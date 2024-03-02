package pro.linguistcopilot.feature.onboarding.page

import com.arkivanov.decompose.ComponentContext

interface OnboardingPageComponent {
    val message: String
    val index: Int

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            message: String,
            index: Int
        ): OnboardingPageComponent
    }
}