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
import java.text.SimpleDateFormat
import java.util.Date
import java.util.UUID
import javax.inject.Inject
import kotlin.random.Random

val sampleList: List<BookItem> by lazy {
    val dateFormatter = SimpleDateFormat("yyyy-MM-dd")
    val startDate = dateFormatter.parse("2023-11-23")!!
    val endDate = dateFormatter.parse("2023-11-27")!!

    (1..10).map {
        val title = "Book $it"
        val uri = UUID.randomUUID().toString()
        val createdAt = generateRandomDate(startDate, endDate)
        val hash = UUID.randomUUID().toString()

        BookItem(title = title, uri = uri, createdAt = createdAt, hash = hash)
    }
}

private fun generateRandomDate(startDate: Date, endDate: Date): Date {
    val random = Random.nextLong(startDate.time, endDate.time)
    return Date(random)
}


@AppComponentScope
class BookItemRepositoryImpl @Inject constructor(
    private val bookItemDao: BookItemDao
) : BookItemRepository {
    private val temporaryBookList =
        MutableStateFlow(sampleList)
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
}
