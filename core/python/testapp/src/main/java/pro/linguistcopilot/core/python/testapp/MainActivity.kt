package pro.linguistcopilot.core.python.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import pro.linguistcopilot.core.python.PySpacyPassiveVoiceHandler
import pro.linguistcopilot.core.python.startPython

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        startPython()

        val text = """
    A scathing review was written by the critic. The house will be cleaned by me every Saturday. Who eaten the last cookie? A safety video will be watched by the staff every year. The application for a new job was faxed by her. The baby was carried by the kangaroo in her pouch.
        """
        val result = PySpacyPassiveVoiceHandler().handlePassiveVoice(text)
        println(result)

        setContent {
            Text("Android")
        }
    }
}