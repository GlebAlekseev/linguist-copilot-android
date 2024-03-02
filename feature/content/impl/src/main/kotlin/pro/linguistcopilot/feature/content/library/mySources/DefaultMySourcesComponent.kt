package pro.linguistcopilot.feature.content.library.mySources

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultMySourcesComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onBookDownload") override val onBookDownload: () -> Unit,
    @Assisted("onOpenBookDescription") override val onOpenBookDescription: () -> Unit,
) : MySourcesComponent, ComponentContext by componentContext {
    @AssistedFactory
    interface Factory : MySourcesComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onBookDownload") onBookDownload: () -> Unit,
            @Assisted("onOpenBookDescription") onOpenBookDescription: () -> Unit
        ): DefaultMySourcesComponent
    }
}