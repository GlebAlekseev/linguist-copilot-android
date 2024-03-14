package pro.linguistcopilot.feature.bookDescription

import android.content.Context
import android.content.Intent
import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionState
import java.io.File

private fun Context.openDocumentInExternalApp(uri: Uri, mimeType: String) {
    val uri = FileProvider.getUriForFile(this,"${packageName}.fileprovider", File(uri.path!!))
    val intent = Intent(Intent.ACTION_VIEW).apply {
        setDataAndType(uri, mimeType)
        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
    }
    startActivity(intent)
}

@Composable
fun BookDescriptionContent(component: BookDescriptionComponent) {
    val state by component.states.collectAsState(initial = component.currentState)
    val context = LocalContext.current
    Column {
        Button(onClick = component.onCloseBookDescription) {
            Text(text = "Назад")
        }
        when (val state = state) {
            is BookDescriptionState.Idle -> {
                Button(onClick = {
                    component.onOpenBookReader.invoke(state.bookItem.id)
                }) {
                    Text(text = "Открыть в читалке")
                }
                Text(text = "Название: ${state.bookItem.title}")
                Text(text = "Original Uri: ${state.bookItem.uri}")
                Text(text = "Epub Uri: ${state.bookItem.epubUri}")
                Text(text = "Pdf Uri: ${state.bookItem.pdfUri}")

                Button(onClick = {
                    val isEpub = state.bookItem.uri.endsWith(".epub")
                    context.openDocumentInExternalApp(
                        Uri.parse(state.bookItem.uri),
                        if (isEpub) "application/epub+zip" else "application/pdf"
                    )
                }) {
                    Text(text = "Открыть origin в другой читалке")
                }
                Button(onClick = {
                    context.openDocumentInExternalApp(
                        Uri.parse(state.bookItem.epubUri),
                        "application/epub+zip"
                    )

                }) {
                    Text(text = "Открыть epub в другой читалке")
                }
                Button(onClick = {
                    context.openDocumentInExternalApp(
                        Uri.parse(state.bookItem.pdfUri),
                        "application/pdf"
                    )
                }) {
                    Text(text = "Открыть pdf в другой читалке")
                }
            }

            BookDescriptionState.Init -> {}
            BookDescriptionState.Loading -> {
                CircularProgressIndicator(color = Color.Black)
            }
        }
    }
}