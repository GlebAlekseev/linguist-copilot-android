package pro.linguistcopilot.feature.book

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.repository.BookItemRepository
import pro.linguistcopilot.feature.book.room.BookItemDao
import pro.linguistcopilot.feature.book.room.mapper.mapToDb
import pro.linguistcopilot.feature.book.room.mapper.mapToDomain
import javax.inject.Inject


@AppComponentScope
class BookItemRepositoryImpl @Inject constructor(
    private val bookItemDao: BookItemDao
) : BookItemRepository {
    private val temporaryBookList =
        MutableStateFlow(listOf<BookItem>())
    private val bookList = bookItemDao.getAllAsFlow().map { it.map { it.mapToDomain() } }
    override val bookItems: Flow<List<BookItem>>
        get() = combine(bookList, temporaryBookList) { dbList, tempList ->
            (dbList + tempList).sortedByDescending { it.createdAt }
        }

    override fun addBookItem(bookItem: BookItem) {
        bookItemDao.insertOrReplace(bookItem.mapToDb())
    }

    override fun addTemporaryBookItem(bookItem: BookItem): BookItem {
        temporaryBookList.value += bookItem
        return bookItem
    }

    override fun removeTemporaryBookItemById(id: String) {
        val newList = temporaryBookList.value.toMutableList().apply {
            removeIf { it.id == id }
        }
        temporaryBookList.tryEmit(newList)
    }

    override fun updateTemporaryBookItem(bookItem: BookItem) {
        val newList = temporaryBookList.value.toMutableList().apply {
            removeIf { it.id == bookItem.id }
            add(bookItem)
        }
        temporaryBookList.tryEmit(newList)
    }

    override suspend fun isExist(hash: String): Boolean {
        return bookItems.first().find { it.hash == hash } != null
    }

    override fun removeTemporaryBookItemAndAddBookItem(bookItem: BookItem) {
        val newList = temporaryBookList.value.toMutableList().apply {
            removeIf { it.id == bookItem.id }
        }
        temporaryBookList.tryEmit(newList)
        bookItemDao.insertOrReplace(bookItem.mapToDb())
    }

    override fun getBookItemById(id: String): BookItem? {
        return bookItemDao.getById(id)?.mapToDomain()
    }
}
