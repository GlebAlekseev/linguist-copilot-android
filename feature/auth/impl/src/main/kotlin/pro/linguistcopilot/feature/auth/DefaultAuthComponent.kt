package pro.linguistcopilot.feature.auth

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultAuthComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseAuth") override val onCloseAuth: () -> Unit
) : AuthComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : AuthComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseAuth") onCloseAuth: () -> Unit
        ): DefaultAuthComponent
    }
}