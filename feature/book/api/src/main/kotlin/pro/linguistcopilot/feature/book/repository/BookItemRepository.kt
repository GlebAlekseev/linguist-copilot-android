package pro.linguistcopilot.feature.book.repository

import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.book.entity.BookItem

interface BookItemRepository {
    val bookItems: Flow<List<BookItem>>
    fun addBookItem(bookItem: BookItem)
}