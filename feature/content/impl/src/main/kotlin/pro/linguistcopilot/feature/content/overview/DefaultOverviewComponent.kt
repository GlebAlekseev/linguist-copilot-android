package pro.linguistcopilot.feature.content.overview

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultOverviewComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
) : OverviewComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : OverviewComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext
        ): DefaultOverviewComponent
    }
}