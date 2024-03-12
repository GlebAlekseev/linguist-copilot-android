package pro.linguistcopilot.feature.bookDescription

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import pro.linguistcopilot.core.elm.RetainedElmStore
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionActor
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionEvent
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionReducer
import pro.linguistcopilot.feature.bookDescription.elm.BookDescriptionState

class DefaultBookDescriptionComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("bookId") private val bookId: String,
    @Assisted("onCloseBookDescription") override val onCloseBookDescription: () -> Unit,
    @Assisted("onOpenBookReader") override val onOpenBookReader: (String) -> Unit,
    private val bookDescriptionReducer: BookDescriptionReducer,
    private val bookDescriptionActor: BookDescriptionActor,
) : BookDescriptionComponent, ComponentContext by componentContext {
    private val retainedElmStore = instanceKeeper.getOrCreate {
        RetainedElmStore(
            startEvent = BookDescriptionEvent.Ui.Start(bookId = bookId),
            initialState = BookDescriptionState.Init,
            reducer = bookDescriptionReducer,
            actor = bookDescriptionActor
        ).also { it.start() }
    }

    @AssistedFactory
    interface Factory : BookDescriptionComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("bookId") bookId: String,
            @Assisted("onCloseBookDescription") onCloseBookDescription: () -> Unit,
            @Assisted("onOpenBookReader") onOpenBookReader: (String) -> Unit
        ): DefaultBookDescriptionComponent
    }

    override val currentState get() = retainedElmStore.currentState
    override val states get() = retainedElmStore.states()
    override val effects get() = retainedElmStore.effects()
    override fun accept(event: BookDescriptionEvent) = retainedElmStore.accept(event)
}