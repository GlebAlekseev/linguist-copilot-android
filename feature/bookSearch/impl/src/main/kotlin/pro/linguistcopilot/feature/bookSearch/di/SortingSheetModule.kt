package pro.linguistcopilot.feature.bookSearch.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookSearch.sortingSheet.DefaultSortingSheetComponent
import pro.linguistcopilot.feature.bookSearch.sortingSheet.SortingSheetComponent

@Module
interface SortingSheetModule {

    @Binds
    fun componentFactory(impl: DefaultSortingSheetComponent.Factory): SortingSheetComponent.Factory
}
