package pro.linguistcopilot.feature.bookDescription.elm

import pro.linguistcopilot.feature.book.entity.BookItem


sealed class BookDescriptionState {
    data object Init : BookDescriptionState()
    data object Loading : BookDescriptionState()
    data class Idle(val bookItem: BookItem) : BookDescriptionState()
}
