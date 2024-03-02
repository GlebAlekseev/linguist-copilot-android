package pro.linguistcopilot.feature.content.library.my_sources

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultMySourcesComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : MySourcesComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : MySourcesComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ): DefaultMySourcesComponent
    }
}