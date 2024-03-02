package pro.linguistcopilot.feature.bookDescription.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.bookDescription.BookDescriptionComponent
import pro.linguistcopilot.feature.bookDescription.DefaultBookDescriptionComponent

@Module
interface BookDescriptionModule {

    @Binds
    fun componentFactory(impl: DefaultBookDescriptionComponent.Factory): BookDescriptionComponent.Factory
}
