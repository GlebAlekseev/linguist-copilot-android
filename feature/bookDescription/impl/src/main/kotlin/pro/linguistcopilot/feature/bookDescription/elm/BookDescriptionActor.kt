package pro.linguistcopilot.feature.bookDescription.elm

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.linguistcopilot.feature.book.usecase.GetBookItemUseCase
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject


class BookDescriptionActor @Inject constructor(
    private val getBookItemUseCase: GetBookItemUseCase,
) : Actor<BookDescriptionCommand, BookDescriptionEvent.Internal> {
    override fun execute(command: BookDescriptionCommand): Flow<BookDescriptionEvent.Internal> {
        return when (val command = command) {
            is BookDescriptionCommand.GetBookItem -> flow {
                val item = getBookItemUseCase.invoke(GetBookItemUseCase.Params(command.bookId))
                emit(
                    BookDescriptionEvent.Internal.Loaded(item)
                )
            }
        }
    }
}