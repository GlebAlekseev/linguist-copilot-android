package pro.linguistcopilot.feature.bookReader

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookReaderContent(component: BookReaderComponent) {
    Column {
        Button(onClick = component.onCloseBookReader) {
            Text(text = "Назад")
        }
        Text(text = "Содержимое книги")
    }
}