package pro.linguistcopilot.feature.book.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import pro.linguistcopilot.core.utils.UseCaseNoParam
import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.repository.BookItemRepository
import javax.inject.Inject

class GetGoodBookListUseCase @Inject constructor(
    private val bookItemRepository: BookItemRepository
) : UseCaseNoParam<Flow<List<BookItem>>>() {
    override suspend fun execute(params: Params.None) =
        bookItemRepository.bookItems.map { it.filter { !it.isBad } }.flowOn(Dispatchers.IO)
}