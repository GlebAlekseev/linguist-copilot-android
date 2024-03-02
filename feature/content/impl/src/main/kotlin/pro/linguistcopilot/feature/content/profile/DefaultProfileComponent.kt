package pro.linguistcopilot.feature.content.profile

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultProfileComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : ProfileComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : ProfileComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ): DefaultProfileComponent
    }
}