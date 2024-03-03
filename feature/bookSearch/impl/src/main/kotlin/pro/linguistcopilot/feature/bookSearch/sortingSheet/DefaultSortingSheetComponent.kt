package pro.linguistcopilot.feature.bookSearch.sortingSheet

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultSortingSheetComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onDismissRequest") val onDismissRequest: () -> Unit,
) : SortingSheetComponent, ComponentContext by componentContext {

    override fun onDismissRequest() {
        onDismissRequest.invoke()
    }

    @AssistedFactory
    interface Factory : SortingSheetComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onDismissRequest") onDismissRequest: () -> Unit,
        ): DefaultSortingSheetComponent
    }
}