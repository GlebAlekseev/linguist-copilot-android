package pro.linguistcopilot.core.python.testapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
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
        val senteces = """
                The cake was baked by Mary.
                The letter has been sent by John.
                The problem will be solved by the team.
                The house was painted by the workers yesterday.
                The book has been read by many students.
                The door will be opened by the manager.
                The cake was delicious.
                Mary baked the cake.
        """.trimIndent().split("\n")
        for (sentece in senteces){
            val result = PyNltkIsPassiveVoice().isPassiveVoice(sentece)
            println("$sentece --> $result")
        }



        setContent {
            Text("Android")
        }
    }
}