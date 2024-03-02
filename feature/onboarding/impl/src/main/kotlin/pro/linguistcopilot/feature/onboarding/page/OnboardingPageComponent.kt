package pro.linguistcopilot.feature.onboarding.page

import com.arkivanov.decompose.ComponentContext

interface OnboardingPageComponent {
    val message: String
    val index: Int
    val onCloseOnboarding: (() -> Unit)?

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            message: String,
            index: Int,
            onCloseOnboarding: (() -> Unit)?,
        ): OnboardingPageComponent
    }
}