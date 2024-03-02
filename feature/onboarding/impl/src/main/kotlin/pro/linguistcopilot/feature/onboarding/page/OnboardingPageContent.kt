package pro.linguistcopilot.feature.onboarding.page

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color

val colors = listOf(Color.Blue, Color.Magenta, Color.Red)

@Composable
fun OnboardingPageContent(component: OnboardingPageComponent) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                colors.get(component.index)
            ), contentAlignment = Alignment.Center
    ) {
        Column {
            Text(text = component.message)
            component.onCloseOnboarding?.let { onCloseOnboarding ->
                Button(onClick = onCloseOnboarding) {
                    Text(text = "Начать")
                }
            }
        }
    }
}