package pro.linguistcopilot.feature.bookDownload.elm

import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.linguistcopilot.feature.book.usecase.GetBookListUseCase
import pro.linguistcopilot.feature.book.usecase.LoadBookUseCase
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject


class BookDownloadActor @Inject constructor(
    private val getBookListUseCase: GetBookListUseCase,
    private val loadBookUseCase: LoadBookUseCase,
) : Actor<BookDownloadCommand, BookDownloadEvent.Internal> {
    override fun execute(command: BookDownloadCommand): Flow<BookDownloadEvent.Internal> {
        return when (command) {
            is BookDownloadCommand.GetList -> flow {
                delay(1000)
                getBookListUseCase.invoke().collect {
                    emit(
                        BookDownloadEvent.Internal.Loaded(it)
                    )
                }
            }

            is BookDownloadCommand.LoadBook -> flow {
                loadBookUseCase.invoke(LoadBookUseCase.Params(command.uri.toString()))
            }
        }
    }
}