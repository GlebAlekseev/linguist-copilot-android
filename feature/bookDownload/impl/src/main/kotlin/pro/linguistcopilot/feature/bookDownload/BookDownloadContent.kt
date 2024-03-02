package pro.linguistcopilot.feature.bookDownload

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun BookDownloadContent(component: BookDownloadComponent) {
    Column {
        Button(onClick = component.onCloseBookDownload) {
            Text(text = "Назад")
        }
        Button(onClick = {}) {
            Text(text = "Загрузить локально с устройства")
        }
        Button(onClick = {}) {
            Text(text = "Загрузить по локальной сети")
        }
    }
}