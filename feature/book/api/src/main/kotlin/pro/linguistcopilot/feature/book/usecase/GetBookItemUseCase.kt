package pro.linguistcopilot.feature.book.usecase

import pro.linguistcopilot.core.utils.UseCase
import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.repository.BookItemRepository
import javax.inject.Inject

class GetBookItemUseCase @Inject constructor(
    private val bookItemRepository: BookItemRepository
) : UseCase<GetBookItemUseCase.Params, BookItem>() {

    @JvmInline
    value class Params(val bookId: String) : UseCase.Params

    override suspend fun execute(params: Params): BookItem {
        return bookItemRepository.getBookItemById(params.bookId)
            ?: throw RuntimeException("Unknown id")
    }
}