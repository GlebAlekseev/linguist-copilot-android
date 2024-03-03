package pro.linguistcopilot.feature.bookSearch.filtersSheet

import com.arkivanov.decompose.ComponentContext

interface FiltersSheetComponent {
    fun onDismissRequest()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onDismissRequest: () -> Unit,
        ): FiltersSheetComponent
    }
}