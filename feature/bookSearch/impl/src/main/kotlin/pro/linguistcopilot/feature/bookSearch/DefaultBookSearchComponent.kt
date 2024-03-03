package pro.linguistcopilot.feature.bookSearch

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.feature.bookSearch.filtersSheet.DefaultFiltersSheetComponent
import pro.linguistcopilot.feature.bookSearch.filtersSheet.FiltersSheetComponent
import pro.linguistcopilot.feature.bookSearch.sortingSheet.DefaultSortingSheetComponent
import pro.linguistcopilot.feature.bookSearch.sortingSheet.SortingSheetComponent

class DefaultBookSearchComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseBookReader") override val onCloseBookSearch: () -> Unit,
    @Assisted("onOpenBookDescription") override val onOpenBookDescription: () -> Unit,
) : BookSearchComponent, ComponentContext by componentContext {
    private val filtersSheetNavigation = SlotNavigation<FiltersSheetConfig>()
    private val sortingSheetNavigation = SlotNavigation<SortingSheetConfig>()
    override val filtersSheet: Value<ChildSlot<*, FiltersSheetComponent>> =
        childSlot(
            source = filtersSheetNavigation,
            serializer = FiltersSheetConfig.serializer(),
            handleBackButton = true,
            key = "filtersSheetChildSlot",
        ) { config, childComponentContext ->
            DefaultFiltersSheetComponent(
                componentContext = childComponentContext,
            ) {
                filtersSheetNavigation.dismiss { }
            }
        }
    override val sortingSheet: Value<ChildSlot<*, SortingSheetComponent>> =
        childSlot(
            source = sortingSheetNavigation,
            serializer = SortingSheetConfig.serializer(),
            key = "sortingSheetChildSlot",
            handleBackButton = true,
        ) { config, childComponentContext ->
            DefaultSortingSheetComponent(
                componentContext = childComponentContext,
            ) {
                sortingSheetNavigation.dismiss { }
            }
        }

    override fun showFilters() {
        filtersSheetNavigation.activate(FiltersSheetConfig)
    }

    override fun showSorting() {
        sortingSheetNavigation.activate(SortingSheetConfig)
    }

    @Serializable
    private object FiltersSheetConfig

    @Serializable
    private object SortingSheetConfig

    @AssistedFactory
    interface Factory : BookSearchComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseBookReader") onCloseBookSearch: () -> Unit,
            @Assisted("onOpenBookDescription") onOpenBookDescription: () -> Unit,
        ): DefaultBookSearchComponent
    }
}