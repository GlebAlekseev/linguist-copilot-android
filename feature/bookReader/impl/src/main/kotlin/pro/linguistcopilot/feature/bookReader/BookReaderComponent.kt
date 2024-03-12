package pro.linguistcopilot.feature.bookReader

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext


@Stable
interface BookReaderComponent {
    val onCloseBookReader: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            bookId: String,
            onCloseBookReader: () -> Unit,
        ): BookReaderComponent
    }
}