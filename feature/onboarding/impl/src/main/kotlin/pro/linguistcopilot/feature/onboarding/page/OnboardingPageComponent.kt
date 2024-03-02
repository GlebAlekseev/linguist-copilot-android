package pro.linguistcopilot.feature.onboarding.page

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
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