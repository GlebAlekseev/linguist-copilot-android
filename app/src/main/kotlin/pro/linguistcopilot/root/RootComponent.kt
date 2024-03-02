package pro.linguistcopilot.root

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.feature.auth.AuthComponent
import pro.linguistcopilot.feature.content.ContentComponent
import pro.linguistcopilot.feature.onboarding.OnboardingComponent

@Stable
interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class Onboarding(val onboardingComponent: OnboardingComponent) : Child()
        class Auth(val authComponent: AuthComponent) : Child()
        class Content(val contentComponent: ContentComponent) : Child()
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}