package pro.linguistcopilot.feature.bookDownload.elm

import pro.linguistcopilot.feature.book.entity.BookItem

sealed class BookDownloadState {
    data object Init : BookDownloadState()
    data object Loading : BookDownloadState()
    data class Idle(val list: List<BookItem>) : BookDownloadState()
}
