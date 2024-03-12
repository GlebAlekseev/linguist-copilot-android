package pro.linguistcopilot.feature.bookDescription

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionEffect
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionEvent
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionState


@Stable
interface BookDescriptionComponent {
    val onCloseBookDescription: () -> Unit
    val onOpenBookReader: (String) -> Unit

    val currentState: BookDescriptionState
    val states: Flow<BookDescriptionState>
    val effects: Flow<BookDescriptionEffect>
    fun accept(event: BookDescriptionEvent)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            bookId: String,
            onCloseBookDescription: () -> Unit,
            onOpenBookReader: (String) -> Unit
        ): BookDescriptionComponent
    }
}