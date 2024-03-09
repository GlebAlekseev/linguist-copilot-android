package pro.linguistcopilot.feature.bookDownload.loadVariantSheet

import android.net.Uri
import com.arkivanov.decompose.ComponentContext

interface LoadVariantSheetComponent {
    fun onDismissRequest()
    fun onLoadUri(uri: Uri)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onDismissRequest: () -> Unit,
            onLoadUri: (uri: Uri) -> Unit,
        ): LoadVariantSheetComponent
    }
}