@file:OptIn(ExperimentalDecomposeApi::class)

package pro.linguistcopilot.feature.onboarding

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.extensions.compose.jetpack.pages.Pages
import com.arkivanov.decompose.extensions.compose.jetpack.pages.PagesScrollAnimation
import pro.linguistcopilot.feature.onboarding.page.OnboardingPageContent

@Composable
fun OnboardingContent(component: OnboardingComponent) {
    Pages(
        modifier = Modifier.fillMaxSize(),
        pages = component.onboardingPages,
        onPageSelected = component::selectPageByIndex,
        scrollAnimation = PagesScrollAnimation.Default
    ) { _, page ->
        OnboardingPageContent(component = page)
    }
}