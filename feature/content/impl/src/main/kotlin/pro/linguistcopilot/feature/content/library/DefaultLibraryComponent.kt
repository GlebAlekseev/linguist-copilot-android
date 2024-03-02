package pro.linguistcopilot.feature.content.library

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultLibraryComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : LibraryComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : LibraryComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ): DefaultLibraryComponent
    }
}