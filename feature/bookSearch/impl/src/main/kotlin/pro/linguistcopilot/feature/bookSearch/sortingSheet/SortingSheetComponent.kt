package pro.linguistcopilot.feature.bookSearch.sortingSheet

import com.arkivanov.decompose.ComponentContext

interface SortingSheetComponent {
    fun onDismissRequest()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onDismissRequest: () -> Unit,
        ): SortingSheetComponent
    }
}