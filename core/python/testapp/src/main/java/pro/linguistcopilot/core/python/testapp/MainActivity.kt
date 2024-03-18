package pro.linguistcopilot.core.python.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import pro.linguistcopilot.core.python.PySpacySentenceParsing
import pro.linguistcopilot.core.python.startPython

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPython()

        val text = "James went to the corner shop to buy some eggs, milk and bread for breakfast."
        val result = PySpacySentenceParsing().parseSentence(text)
        println(result)

        setContent {
            Text("Android")
        }
    }
}