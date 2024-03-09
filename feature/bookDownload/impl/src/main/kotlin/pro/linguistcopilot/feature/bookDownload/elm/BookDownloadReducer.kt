package pro.linguistcopilot.feature.bookDownload.elm

import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import javax.inject.Inject

class BookDownloadReducer @Inject constructor() :
    ScreenDslReducer<BookDownloadEvent, BookDownloadEvent.Ui, BookDownloadEvent.Internal, BookDownloadState, BookDownloadEffect, BookDownloadCommand>(
        BookDownloadEvent.Ui::class,
        BookDownloadEvent.Internal::class,
    ) {
    override fun Result.internal(event: BookDownloadEvent.Internal) {
        when (event) {
            is BookDownloadEvent.Internal.Loaded -> {
                state {
                    BookDownloadState.Idle(event.list)
                }
            }
        }
    }

    override fun Result.ui(event: BookDownloadEvent.Ui) {
        when (event) {
            BookDownloadEvent.Ui.Start -> {
                state { BookDownloadState.Loading }
                commands { +BookDownloadCommand.GetList }
            }

            is BookDownloadEvent.Ui.Load -> {
                commands { +BookDownloadCommand.LoadBook(event.uri) }
            }
        }
    }
}