package pro.linguistcopilot.feature.bookDownload.elm

import android.net.Uri

sealed class BookDownloadCommand {
    data class LoadBook(val uri: Uri) : BookDownloadCommand()
    data object GetList : BookDownloadCommand()
}