package pro.linguistcopilot.feature.bookSearch

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookSearchContent(component: BookSearchComponent) {
    Column {
        Button(onClick = component.onCloseBookSearch) {
            Text(text = "Назад")
        }
        Button(onClick = component.onOpenBookDescription) {
            Text(text = "Открыть книгу")
        }
        Text(text = "Поиск")
    }
}