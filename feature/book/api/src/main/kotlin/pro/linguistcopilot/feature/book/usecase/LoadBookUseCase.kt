package pro.linguistcopilot.feature.book.usecase

import pro.linguistcopilot.core.utils.UseCase
import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.repository.BookItemRepository
import java.text.SimpleDateFormat
import javax.inject.Inject

class LoadBookUseCase @Inject constructor(
    private val bookItemRepository: BookItemRepository
) : UseCase<LoadBookUseCase.Params, Unit>() {
    override suspend fun execute(params: Params) {
        bookItemRepository.addBookItem(
            BookItem(
                title = "Book ${params.uri.take(6)}",
                SimpleDateFormat("dd.MM.yyyy").parse("01.01.2024")
            )
        )
    }

    @JvmInline
    value class Params(val uri: String) : UseCase.Params
}
