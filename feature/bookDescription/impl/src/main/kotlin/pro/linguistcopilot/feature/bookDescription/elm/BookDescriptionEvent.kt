package pro.linguistcopilot.feature.bookDescription.elm

import pro.linguistcopilot.feature.book.entity.BookItem


sealed class BookDescriptionEvent {
    sealed class Ui : BookDescriptionEvent() {
        data class Start(val bookId: String) : Ui()
    }

    sealed class Internal : BookDescriptionEvent() {
        data class Loaded(val bookItem: BookItem) : Internal()
    }
}