package pro.linguistcopilot.feature.book

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import pro.linguistcopilot.di.scope.AppComponentScope
import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.repository.BookItemRepository
import java.text.SimpleDateFormat
import javax.inject.Inject

val sampleList = listOf(
    BookItem("Book 1", SimpleDateFormat("dd.MM.yyyy").parse("29.12.2023")),
    BookItem("Book 2", SimpleDateFormat("dd.MM.yyyy").parse("29.12.2023")),
    BookItem("Book 3", SimpleDateFormat("dd.MM.yyyy").parse("29.12.2023")),
    BookItem("Book 4", SimpleDateFormat("dd.MM.yyyy").parse("29.12.2023")),
    BookItem("Book 5", SimpleDateFormat("dd.MM.yyyy").parse("29.12.2023")),
    BookItem("Book 6", SimpleDateFormat("dd.MM.yyyy").parse("30.12.2023")),
    BookItem("Book 7", SimpleDateFormat("dd.MM.yyyy").parse("30.12.2023")),
    BookItem("Book 8", SimpleDateFormat("dd.MM.yyyy").parse("30.12.2023")),
    BookItem("Book 9", SimpleDateFormat("dd.MM.yyyy").parse("30.12.2023")),
    BookItem("Book 10", SimpleDateFormat("dd.MM.yyyy").parse("30.12.2023")),
    BookItem("Book 11", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 12", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 13", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 14", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 15", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 16", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 17", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 18", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 19", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023")),
    BookItem("Book 20", SimpleDateFormat("dd.MM.yyyy").parse("31.12.2023"))
)


@AppComponentScope
class BookItemRepositoryImpl @Inject constructor() : BookItemRepository {
    private val bookList = MutableStateFlow(sampleList)
    override val bookItems: Flow<List<BookItem>> = bookList
    override fun addBookItem(bookItem: BookItem) {
        bookList.tryEmit(bookList.value + bookItem)
    }
}