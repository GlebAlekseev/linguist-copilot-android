package pro.linguistcopilot.feature.bookSearch.filtersSheet

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultFiltersSheetComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onDismissRequest") val onDismissRequest: () -> Unit,
) : FiltersSheetComponent, ComponentContext by componentContext {

    override fun onDismissRequest() {
        onDismissRequest.invoke()
    }

    @AssistedFactory
    interface Factory : FiltersSheetComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onDismissRequest") onDismissRequest: () -> Unit,
        ): DefaultFiltersSheetComponent
    }
}