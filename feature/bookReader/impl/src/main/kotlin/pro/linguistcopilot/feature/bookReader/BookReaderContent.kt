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
    val controller = component.textProcessingController
    val lang = controller.getTextLanguage("The quick brown fox jumps over the lazy dog.")
    val taggedText = controller.getTaggedText("The quick brown fox jumps over the lazy dog.")
    val translateTextUseCase = component.translateTextUseCase
    val text = """
        В лесу за деревней таилось старое загадочное место, о котором ходили мрачные легенды. Густые лианы переплетались среди деревьев, создавая впечатление, что они живые и стремятся удержать любого, кто осмелился приблизиться. Ветер шептал таинственные слова, а в темноте слышались шорохи, заставляющие сердце биться быстрее. Местные жители рассказывали истории о потерянных путниках, которые исчезали без вести в этих зловещих чащах, никогда не вернувшись к своим домам. Но несмотря на все предостережения, одинокий исследователь решил пойти на риск и узнать, что скрывается в глубинах этого таинственного леса...
      
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
        Text(text = lang.toString())
        Text(text = taggedText.toString())
    }
}