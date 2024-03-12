package pro.linguistcopilot.feature.bookDescription.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import javax.inject.Inject

class BookDescriptionReducer @Inject constructor() :
    ScreenDslReducer<BookDescriptionEvent, BookDescriptionEvent.Ui, BookDescriptionEvent.Internal, BookDescriptionState, BookDescriptionEffect, BookDescriptionCommand>(
        BookDescriptionEvent.Ui::class,
        BookDescriptionEvent.Internal::class,
    ) {
    override fun Result.internal(event: BookDescriptionEvent.Internal) {
        when (event) {
            is BookDescriptionEvent.Internal.Loaded -> {
                state {
                    BookDescriptionState.Idle(event.bookItem)
                }
            }
        }
    }

    override fun Result.ui(event: BookDescriptionEvent.Ui) {
        when (val event = event) {
            is BookDescriptionEvent.Ui.Start -> {
                state { BookDescriptionState.Loading }
                commands { +BookDescriptionCommand.GetBookItem(event.bookId) }
            }
        }
    }
}