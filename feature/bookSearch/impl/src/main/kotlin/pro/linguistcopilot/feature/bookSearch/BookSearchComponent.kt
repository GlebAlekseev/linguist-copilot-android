package pro.linguistcopilot.feature.bookSearch

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.feature.bookSearch.filtersSheet.FiltersSheetComponent
import pro.linguistcopilot.feature.bookSearch.sortingSheet.SortingSheetComponent


@Stable
interface BookSearchComponent {
    val onCloseBookSearch: () -> Unit
    val onOpenBookDescription: (String) -> Unit

    val filtersSheet: Value<ChildSlot<*, FiltersSheetComponent>>
    val sortingSheet: Value<ChildSlot<*, SortingSheetComponent>>
    fun showFilters()
    fun showSorting()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseBookSearch: () -> Unit,
            onOpenBookDescription: (String) -> Unit,
        ): BookSearchComponent
    }
}