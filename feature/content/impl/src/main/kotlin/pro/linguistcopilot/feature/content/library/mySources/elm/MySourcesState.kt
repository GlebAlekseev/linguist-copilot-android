package pro.linguistcopilot.feature.content.library.mySources.elm

import pro.linguistcopilot.feature.book.entity.BookItem


sealed class MySourcesState {
    data object Init : MySourcesState()
    data object Loading : MySourcesState()
    data class Idle(val list: List<BookItem>) : MySourcesState()
}
