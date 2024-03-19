package pro.linguistcopilot.feature.bookReader

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import pro.linguistcopilot.feature.translate.entity.Language

@Composable
fun BookReaderContent(component: BookReaderComponent) {
    val controller = component.textProcessingController
    val lang = controller.getTextLanguage("The quick brown fox jumps over the lazy dog.")
    val taggedText = controller.getTaggedText("The quick brown fox jumps over the lazy dog.")
    val ctrl = component.textTranslationController
    val text = """
        В лесу за деревней таилось старое загадочное место, о котором ходили мрачные легенды. Густые лианы переплетались среди деревьев, создавая впечатление, что они живые и стремятся удержать любого, кто осмелился приблизиться. Ветер шептал таинственные слова, а в темноте слышались шорохи, заставляющие сердце биться быстрее. Местные жители рассказывали истории о потерянных путниках, которые исчезали без вести в этих зловещих чащах, никогда не вернувшись к своим домам. Но несмотря на все предостережения, одинокий исследователь решил пойти на риск и узнать, что скрывается в глубинах этого таинственного леса...
        
    """.trimIndent()
    val source = ctrl.availableLanguages.find { it is Language.Russian }!!
    val target = ctrl.availableLanguages.find { it is Language.English }!!
    LaunchedEffect(Any()) {
        CoroutineScope(Dispatchers.IO).launch {
            val translatedText = ctrl.translate(text, sourceLanguage = source, targetLanguage = target)
            println(translatedText)
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