package pro.linguistcopilot.feature.content.library.mySources.elm

import pro.linguistcopilot.feature.book.entity.BookItem


sealed class MySourcesEvent {
    sealed class Ui : MySourcesEvent() {
        data object Start : Ui()
    }

    sealed class Internal : MySourcesEvent() {
        data class Loaded(val list: List<BookItem>) : Internal()
    }
}