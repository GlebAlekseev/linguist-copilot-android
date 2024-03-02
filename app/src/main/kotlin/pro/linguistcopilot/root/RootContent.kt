package pro.linguistcopilot.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import pro.linguistcopilot.feature.onboarding.OnboardingContent

@Composable
fun RootContent(component: RootComponent) {
    Children(
        modifier = Modifier.fillMaxSize(),
        stack = component.stack,
        animation = stackAnimation(animator = fade() + scale()),
    ) {
        when (val child = it.instance) {
            is RootComponent.Child.Onboarding -> OnboardingContent(component = child.onboardingComponent)
        }
    }
}