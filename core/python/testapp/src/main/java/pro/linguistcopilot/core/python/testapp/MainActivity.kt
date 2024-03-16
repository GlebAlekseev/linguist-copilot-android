package pro.linguistcopilot.core.python.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import pro.linguistcopilot.core.python.PyNltkCategorizeText
import pro.linguistcopilot.core.python.PyNltkIsPassiveVoice
import pro.linguistcopilot.core.python.PyNltkKeywordExtraction
import pro.linguistcopilot.core.python.PyNltkMostRareWords
import pro.linguistcopilot.core.python.startPython

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPython()
//        val result = PyNltkPOSForText().getPOSForText(
//            "A surface container using the 'background' color from the theme"
//        )
//        println(result)
        val text = "The new programming language is revolutionizing the technology industry."
        val map = mapOf(
            "technology" to listOf("computer", "internet", "software", "programming"),
            "sports" to listOf("football", "soccer", "basketball",  "tennis"),
            "politics" to listOf("government","president","election","policy")
        )
        val result = PyNltkCategorizeText().categorize(text, map)
        println(result)

        setContent {
            Text("Android")
        }
    }
}