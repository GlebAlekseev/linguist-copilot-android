package pro.linguistcopilot.feature.bookDescription

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionState

//private fun Context.openDocumentInExternalApp(uri: Uri) {
//    val intent = Intent(Intent.ACTION_VIEW).apply {
//        setDataAndType(uri, "application/pdf")
//        flags =  Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_GRANT_READ_URI_PERMISSION
//        action = Intent.ACTION_CREATE_DOCUMENT
//    }
//    startActivity(intent)
//}

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
//                    context.openDocumentInExternalApp(Uri.parse(state.bookItem.uri))
                }) {
                    Text(text = "Открыть origin в другой читалке")
                }
                Button(onClick = {
//                    context.openDocumentInExternalApp(Uri.parse(state.bookItem.epubUri))

                }) {
                    Text(text = "Открыть epub в другой читалке")
                }
                Button(onClick = {
//                    context.openDocumentInExternalApp(Uri.parse(state.bookItem.pdfUri))
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