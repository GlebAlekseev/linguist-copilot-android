package pro.linguistcopilot.feature.content

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultContentComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onNavigateToAuth") override val onNavigateToAuth: () -> Unit,
) : ContentComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : ContentComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onNavigateToAuth") onNavigateToAuth: () -> Unit
        ): DefaultContentComponent
    }
}