package pro.linguistcopilot.feature.bookDescription

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookDescriptionContent(component: BookDescriptionComponent) {
    Column {
        Button(onClick = component.onCloseBookDescription) {
            Text(text = "Назад")
        }
        Button(onClick = component.onOpenBookReader) {
            Text(text = "Открыть в читалке")
        }
    }
}