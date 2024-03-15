package pro.linguistcopilot.feature.bookReader

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookReaderContent(component: BookReaderComponent) {
    val controller = component.textProcessingController
    val lang = controller.getTextLanguage("The quick brown fox jumps over the lazy dog.")
    val taggedText = controller.getTaggedText("The quick brown fox jumps over the lazy dog.")
    Column {
        Button(onClick = component.onCloseBookReader) {
            Text(text = "Назад")
        }
        Text(text = "Содержимое книги")
        Text(text = lang.toString())
        Text(text = taggedText.toString())
    }
}