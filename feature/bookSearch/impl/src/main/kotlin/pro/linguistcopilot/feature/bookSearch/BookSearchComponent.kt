package pro.linguistcopilot.feature.bookSearch

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext


@Stable
interface BookSearchComponent {
    val onCloseBookSearch: () -> Unit
    val onOpenBookDescription: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseBookSearch: () -> Unit,
            onOpenBookDescription: () -> Unit,
        ): BookSearchComponent
    }
}