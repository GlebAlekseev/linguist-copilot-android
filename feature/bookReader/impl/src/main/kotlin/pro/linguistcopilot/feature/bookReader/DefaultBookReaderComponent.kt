package pro.linguistcopilot.feature.bookReader

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultBookReaderComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseBookReader") override val onCloseBookReader: () -> Unit,
) : BookReaderComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : BookReaderComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseBookReader") onCloseBookReader: () -> Unit,
        ): DefaultBookReaderComponent
    }
}