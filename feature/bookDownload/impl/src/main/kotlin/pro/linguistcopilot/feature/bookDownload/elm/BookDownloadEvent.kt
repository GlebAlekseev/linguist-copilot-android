package pro.linguistcopilot.feature.bookDownload.elm

import android.net.Uri
import pro.linguistcopilot.feature.book.entity.BookItem

sealed class BookDownloadEvent {
    sealed class Ui : BookDownloadEvent() {
        data object Start : Ui()
        data class Load(val uri: Uri) : Ui()
    }

    sealed class Internal : BookDownloadEvent() {
        data class Loaded(val list: List<BookItem>) : Internal()
    }
}