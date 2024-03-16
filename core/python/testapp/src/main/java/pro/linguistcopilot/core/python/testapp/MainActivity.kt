package pro.linguistcopilot.core.python.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import pro.linguistcopilot.core.python.PyNltkPOSForText
import pro.linguistcopilot.core.python.startPython

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPython()
        val result = PyNltkPOSForText().getPOSForText(
            "A surface container using the 'background' color from the theme"
        )
        println(result)

        setContent {
            Text("Android")
        }
    }
}