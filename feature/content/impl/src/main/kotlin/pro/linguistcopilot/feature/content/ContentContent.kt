package pro.linguistcopilot.feature.content

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ContentContent(component: ContentComponent) {
    Column {
        Text(text = "Content Screen")
        Button(onClick = component.onNavigateToAuth) {
            Text(text = "Перейти к авторизации")
        }
    }
}