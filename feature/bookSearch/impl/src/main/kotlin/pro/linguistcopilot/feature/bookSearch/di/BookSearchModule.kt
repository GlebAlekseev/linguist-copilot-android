package pro.linguistcopilot.feature.bookSearch.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookSearch.BookSearchComponent
import pro.linguistcopilot.feature.bookSearch.DefaultBookSearchComponent

@Module(includes = [SortingSheetModule::class, FiltersSheetModule::class])
interface BookSearchModule {

    @Binds
    fun componentFactory(impl: DefaultBookSearchComponent.Factory): BookSearchComponent.Factory
}
