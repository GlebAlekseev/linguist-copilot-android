package pro.linguistcopilot.feature.bookDescription

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext


@Stable
interface BookDescriptionComponent {
    val onCloseBookDescription: () -> Unit
    val onOpenBookReader: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseBookDescription: () -> Unit,
            onOpenBookReader: () -> Unit
        ): BookDescriptionComponent
    }
}