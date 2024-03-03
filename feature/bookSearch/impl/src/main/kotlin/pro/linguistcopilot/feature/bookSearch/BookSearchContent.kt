package pro.linguistcopilot.feature.bookSearch

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import com.arkivanov.decompose.extensions.compose.jetpack.subscribeAsState
import pro.linguistcopilot.feature.bookSearch.filtersSheet.FiltersSheetContent
import pro.linguistcopilot.feature.bookSearch.sortingSheet.SortingSheetContent

@Composable
fun BookSearchContent(component: BookSearchComponent) {
    Column {
        Button(onClick = component.onCloseBookSearch) {
            Text(text = "Назад")
        }
        Button(onClick = component.onOpenBookDescription) {
            Text(text = "Открыть книгу")
        }
        Button(onClick = component::showFilters) {
            Text(text = "Фильтры")
        }
        Button(onClick = component::showSorting) {
            Text(text = "Сортировка")
        }
        Text(text = "Поиск")

        val sortingSheetSlot by component.sortingSheet.subscribeAsState()
        sortingSheetSlot.child?.instance?.also {
            SortingSheetContent(component = it)
        }
        val filtersSheetSlot by component.filtersSheet.subscribeAsState()
        filtersSheetSlot.child?.instance?.also {
            FiltersSheetContent(component = it)
        }
    }
}