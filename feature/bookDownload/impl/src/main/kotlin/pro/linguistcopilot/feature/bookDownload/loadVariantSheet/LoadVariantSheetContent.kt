package pro.linguistcopilot.feature.bookDownload.loadVariantSheet

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color


@Composable
fun LoadVariantSheetContent(component: LoadVariantSheetComponent) {
    val pickFileLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val selectedFileUri: Uri? = result.data?.data
            selectedFileUri?.let {
                component.onLoadUri(it)
            }
            component.onDismissRequest()
        }
    }
    ModalBottomSheet(
        containerColor = Color.Gray,
        onDismissRequest = {
            component.onDismissRequest()
        },
    ) {
        Column {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_GET_CONTENT).apply {
                    type = "*/*"
                    addCategory(Intent.CATEGORY_OPENABLE)
                }
                pickFileLauncher.launch(intent)
            }) {
                Text(text = "Загрузить с устройства")
            }
            Button(onClick = { /*TODO*/ }) {
                Text(text = "Загрузить по локальной сети")
            }
        }
    }
}