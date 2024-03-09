package pro.linguistcopilot.feature.bookDownload

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext


@Stable
interface BookDownloadComponent {
    val onCloseBookDownload: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseBookDownload: () -> Unit
        ): BookDownloadComponent
    }
}