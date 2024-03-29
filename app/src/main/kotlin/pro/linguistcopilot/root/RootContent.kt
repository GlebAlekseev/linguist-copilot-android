package pro.linguistcopilot.root

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.extensions.compose.jetpack.stack.Children
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetpack.stack.animation.stackAnimation
import pro.linguistcopilot.feature.auth.AuthContent
import pro.linguistcopilot.feature.bookDescription.BookDescriptionContent
import pro.linguistcopilot.feature.bookDownload.BookDownloadContent
import pro.linguistcopilot.feature.bookReader.BookReaderContent
import pro.linguistcopilot.feature.bookSearch.BookSearchContent
import pro.linguistcopilot.feature.content.ContentContent
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
            is RootComponent.Child.Auth -> AuthContent(component = child.authComponent)
            is RootComponent.Child.Content -> ContentContent(component = child.contentComponent)
            is RootComponent.Child.BookDownload -> BookDownloadContent(component = child.bookDownloadComponent)
            is RootComponent.Child.BookDescription -> BookDescriptionContent(component = child.bookDescriptionComponent)
            is RootComponent.Child.BookReader -> BookReaderContent(component = child.bookReaderComponent)
            is RootComponent.Child.BookSearch -> BookSearchContent(component = child.bookSearchComponent)
        }
    }
}