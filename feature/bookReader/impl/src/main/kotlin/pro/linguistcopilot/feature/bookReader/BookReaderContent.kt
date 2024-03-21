package pro.linguistcopilot.feature.bookReader

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.linguistcopilot.feature.translate.usecase.TranslateTextUseCase
import pro.linguistcopilot.feature.word.entity.Language

@Composable
fun BookReaderContent(component: BookReaderComponent) {
    val translateTextUseCase = component.translateTextUseCase
    val text = """
        В лесу за деревней таилось старое загадочное место, о котором ходили мрачные легенды.
      
    """.trimIndent()
    LaunchedEffect(Any()) {
        CoroutineScope(Dispatchers.IO).launch {
            val result = translateTextUseCase.invoke(
                TranslateTextUseCase.Params(
                    text = text,
                    sourceLanguage = Language.Russian,
                    targetLanguage = Language.English,
                    targetTranslationEngineConfig = null
                )
            )
            println(result)
        }
    }
    Column {
        Button(onClick = component.onCloseBookReader) {
            Text(text = "Назад")
        }
        Text(text = "Содержимое книги")
    }
}