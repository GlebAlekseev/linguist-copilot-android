package pro.linguistcopilot.feature.bookDownload

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadState
import pro.linguistcopilot.feature.bookDownload.entity.BookItem
import pro.linguistcopilot.feature.bookDownload.loadVariantSheet.LoadVariantSheetContent
import java.text.SimpleDateFormat

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

@Composable
fun BookDownloadContent(component: BookDownloadComponent) {
    val state by component.states.collectAsState(initial = component.currentState)
    val loadVariantSheetSlot by component.loadVariantSheet.subscribeAsState()
    loadVariantSheetSlot.child?.instance?.let {
        LoadVariantSheetContent(component = it)
    }
    Column {
        Row {
            Button(onClick = component.onCloseBookDownload) {
                Text(text = "Назад")
            }
            if (state !is BookDownloadState.Init) {
                Button(onClick = component::showLoadVariant) {
                    Text(text = "Загрузить")
                }
            }
        }
        when (val state = state) {
            is BookDownloadState.Init -> {}
            is BookDownloadState.Loading -> {
                CircularProgressIndicator(color = Color.Black)
            }

            is BookDownloadState.Idle -> {
                val groupedBookList = state.list.sortedBy { it.date }
                    .groupBy { SimpleDateFormat("dd.MM.yyyy").format(it.date) }

                LazyColumn {
                    items(groupedBookList.toList()) { (date, bookItems) ->
                        Text(text = date)
                        LazyRow {
                            items(bookItems) { bookItem ->
                                Column(modifier = Modifier.padding(horizontal = 8.dp)) {
                                    Box(
                                        modifier = Modifier
                                            .width(50.dp)
                                            .height(100.dp)
                                            .background(Color.Red)
                                    )
                                    Text(text = bookItem.title)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}