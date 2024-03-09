package pro.linguistcopilot.feature.bookDownload.elm

import android.net.Uri
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.flow
import pro.linguistcopilot.feature.bookDownload.entity.BookItem
import pro.linguistcopilot.feature.bookDownload.sampleList
import vivid.money.elmslie.coroutines.Actor
import java.text.SimpleDateFormat
import javax.inject.Inject


class BookDownloadActor @Inject constructor() :
    Actor<BookDownloadCommand, BookDownloadEvent.Internal> {
    private fun newItem(uri: Uri) = BookItem(
        "Book ${uri.lastPathSegment?.takeLast(6)}",
        SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")
    )

    private val listState = MutableStateFlow(sampleList)
    override fun execute(command: BookDownloadCommand): Flow<BookDownloadEvent.Internal> {
        return when (command) {
            is BookDownloadCommand.GetList -> flow {
                delay(1000)
                listState.collect {
                    emit(
                        BookDownloadEvent.Internal.Loaded(
                            it
                        )
                    )
                }
            }

            is BookDownloadCommand.LoadBook -> flow {
                listState.emit(listState.value + newItem(command.uri))
            }
        }
    }
}