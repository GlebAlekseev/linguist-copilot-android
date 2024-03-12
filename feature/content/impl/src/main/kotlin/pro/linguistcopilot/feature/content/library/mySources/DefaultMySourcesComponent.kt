package pro.linguistcopilot.feature.content.library.mySources

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.essenty.instancekeeper.getOrCreate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import pro.linguistcopilot.core.elm.RetainedElmStore
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesActor
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesEvent
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesReducer
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesState

class DefaultMySourcesComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onBookDownload") override val onBookDownload: () -> Unit,
    @Assisted("onOpenBookDescription") override val onOpenBookDescription: (String) -> Unit,
    private val mySourcesReducer: MySourcesReducer,
    private val mySourcesActor: MySourcesActor,
) : MySourcesComponent, ComponentContext by componentContext {
    private val retainedElmStore = instanceKeeper.getOrCreate {
        RetainedElmStore(
            startEvent = MySourcesEvent.Ui.Start,
            initialState = MySourcesState.Init,
            reducer = mySourcesReducer,
            actor = mySourcesActor
        ).also { it.start() }
    }

    override val currentState get() = retainedElmStore.currentState
    override val states get() = retainedElmStore.states()
    override val effects get() = retainedElmStore.effects()
    override fun accept(event: MySourcesEvent) = retainedElmStore.accept(event)

    @AssistedFactory
    interface Factory : MySourcesComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onBookDownload") onBookDownload: () -> Unit,
            @Assisted("onOpenBookDescription") onOpenBookDescription: (String) -> Unit
        ): DefaultMySourcesComponent
    }
}