package pro.linguistcopilot.feature.content.library.external_sources

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultExternalSourcesComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : ExternalSourcesComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : ExternalSourcesComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ): DefaultExternalSourcesComponent
    }
}