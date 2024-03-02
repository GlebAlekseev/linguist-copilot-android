package pro.linguistcopilot.feature.onboarding

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.feature.onboarding.page.OnboardingPageComponent


@Stable
interface OnboardingComponent {
    @OptIn(ExperimentalDecomposeApi::class)
    val onboardingPages: Value<ChildPages<*, OnboardingPageComponent>>

    fun selectPageByIndex(index: Int)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseOnboarding: () -> Unit
        ): OnboardingComponent
    }
}