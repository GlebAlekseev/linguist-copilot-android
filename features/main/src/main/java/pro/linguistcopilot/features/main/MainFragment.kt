package pro.linguistcopilot.features.main

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.fragment.app.Fragment
import pro.linguistcopilot.features.reader.domain.BookUrlArg
import pro.linguistcopilot.navigation.navigate

class MainFragment : Fragment() {
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_OPEN_EBOOK && resultCode == Activity.RESULT_OK) {
            val uri = data?.data
            if (uri != null) {
                navigate(
                    actionId = R.id.action_global_to_readerFragment,
                    data = BookUrlArg(uri.toString())
                )
            }
        }
    }

    fun chooseEpubFile() {
        val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
        intent.type = "application/epub+zip"

        startActivityForResult(intent, REQUEST_CODE_OPEN_EBOOK)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return ComposeView(this.requireContext()).apply {
            setContent {
                Column(
                    modifier = Modifier.fillMaxSize(),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    Button(onClick = {
                        navigate(
                            actionId = R.id.action_global_to_readerFragment,
                            data = BookUrlArg("uri".toString())
                        )
//                        chooseEpubFile()
                    }) {
                        Text(text = "Открыть epub в читалке")
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_OPEN_EBOOK = 30001
    }
}
