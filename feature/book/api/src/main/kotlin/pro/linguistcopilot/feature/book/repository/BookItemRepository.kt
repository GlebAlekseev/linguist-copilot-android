package pro.linguistcopilot.feature.book.repository

import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.book.entity.BookItem

interface BookItemRepository {
    val bookItems: Flow<List<BookItem>>
    fun addBookItem(bookItem: BookItem)
    fun addTemporaryBookItem(bookItem: BookItem): BookItem
    fun removeTemporaryBookItemById(id: String)
    fun updateTemporaryBookItem(bookItem: BookItem)
    suspend fun isExist(hash: String): Boolean
    fun removeTemporaryBookItemAndAddBookItem(bookItem: BookItem)
}