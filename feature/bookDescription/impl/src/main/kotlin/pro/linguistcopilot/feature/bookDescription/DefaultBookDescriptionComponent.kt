package pro.linguistcopilot.feature.bookDescription

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultBookDescriptionComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseBookDownload") override val onCloseBookDownload: () -> Unit,
    @Assisted("onOpenBookReader") override val onOpenBookReader: () -> Unit
) : BookDescriptionComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : BookDescriptionComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseBookDownload") onCloseBookDownload: () -> Unit,
            @Assisted("onOpenBookReader") onOpenBookReader: () -> Unit
        ): DefaultBookDescriptionComponent
    }
}