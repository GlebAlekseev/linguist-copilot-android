package pro.linguistcopilot.feature.bookSearch.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookSearch.filtersSheet.DefaultFiltersSheetComponent
import pro.linguistcopilot.feature.bookSearch.filtersSheet.FiltersSheetComponent

@Module
interface FiltersSheetModule {

    @Binds
    fun componentFactory(impl: DefaultFiltersSheetComponent.Factory): FiltersSheetComponent.Factory
}
