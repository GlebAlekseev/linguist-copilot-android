package pro.linguistcopilot.feature.bookSearch

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultBookSearchComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseBookReader") override val onCloseBookSearch: () -> Unit,
    @Assisted("onOpenBookDescription") override val onOpenBookDescription: () -> Unit,
) : BookSearchComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : BookSearchComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseBookReader") onCloseBookSearch: () -> Unit,
            @Assisted("onOpenBookDescription") onOpenBookDescription: () -> Unit,
        ): DefaultBookSearchComponent
    }
}