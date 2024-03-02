package pro.linguistcopilot.feature.bookDescription

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext


@Stable
interface BookDescriptionComponent {
    val onCloseBookDownload: () -> Unit
    val onOpenBookReader: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseBookDownload: () -> Unit,
            onOpenBookReader: () -> Unit
        ): BookDescriptionComponent
    }
}