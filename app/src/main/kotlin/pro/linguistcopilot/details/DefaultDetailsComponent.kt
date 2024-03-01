package pro.linguistcopilot.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.InstanceKeeper
import com.arkivanov.essenty.instancekeeper.getOrCreate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.linguistcopilot.Item
import vivid.money.elmslie.core.store.DefaultActor
import vivid.money.elmslie.core.store.ElmStore
import vivid.money.elmslie.core.store.StateReducer
import vivid.money.elmslie.core.store.Store
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import vivid.money.elmslie.coroutines.Actor
import javax.inject.Inject


data class TestState(val text: String)
sealed class TestCommand {
    data class Send(val id: Int) : TestCommand()
}

sealed class TestEffect {
    data object NavigateTo : TestEffect()
    data class ShowError(val text: String) : TestEffect()
}

sealed class TestEvent {
    sealed class Ui : TestEvent() {
        data class Start(val itemId: String) : Ui()
    }

    sealed class Internal : TestEvent() {
        data object FinishLoading : Internal()
        data object ErrorLoading : Internal()
    }
}

class TestReducer @Inject constructor() :
    ScreenDslReducer<TestEvent, TestEvent.Ui, TestEvent.Internal, TestState, TestEffect, TestCommand>(
        TestEvent.Ui::class,
        TestEvent.Internal::class
    ) {
    override fun Result.internal(event: TestEvent.Internal) {
        when (event) {
            TestEvent.Internal.FinishLoading -> println("TestEvent.Internal.FinishLoading")
            TestEvent.Internal.ErrorLoading -> println("TestEvent.Internal.ErrorLoading")
        }
    }

    override fun Result.ui(event: TestEvent.Ui) {
        when (event) {
            is TestEvent.Ui.Start -> println("TestEvent.Ui.Start")
        }
    }
}

class TestActor @Inject constructor() : Actor<TestCommand, TestEvent.Internal> {
    override fun execute(command: TestCommand): Flow<TestEvent.Internal> {
        return when (command) {
            is TestCommand.Send -> flow {
                emit(Unit)
            }.mapEvents(TestEvent.Internal.FinishLoading, TestEvent.Internal.ErrorLoading)
        }
    }

}

class DefaultDetailsComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val itemId: String,
    @Assisted private val onFinished: () -> Unit,
    private val testReducer: TestReducer,
    private val testActor: TestActor,
) : DetailsComponent, ComponentContext by componentContext {
    private val retainedElmStore = instanceKeeper.getOrCreate {
        RetainedElmStore(
            startEvent = TestEvent.Ui.Start(itemId),
            initialState = TestState("loading"),
            reducer = testReducer,
            actor = testActor
        ).also { it.start() }
    }

    override val currentState: TestState
        get() = retainedElmStore.currentState

    override val states get() = retainedElmStore.states()
    override val effects get() = retainedElmStore.effects()
    override fun accept(testEvent: TestEvent) = retainedElmStore.accept(testEvent)

    override val item: Value<Item> =
        MutableValue(Item(id = itemId, text = "text $itemId", title = "title $itemId"))

    override fun onCloseClicked() {
        onFinished()
    }

    override fun navigateTo() {

    }

    class RetainedElmStore<Event : Any, State : Any, Effect : Any, Command : Any>(
        initialState: State,
        reducer: StateReducer<Event, State, Effect, Command>,
        actor: Actor<Command, out Event>,
        startEvent: Event? = null,
    ) : Store<Event, Effect, State> by ElmStore(
        initialState = initialState,
        reducer = reducer,
        actor = actor.toDefaultActor(),
        startEvent = startEvent,
    ), InstanceKeeper.Instance

    @AssistedFactory
    interface Factory : DetailsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            itemId: String,
            onFinished: () -> Unit,
        ): DefaultDetailsComponent
    }
}

fun <Command : Any, Event : Any> Actor<Command, Event>.toDefaultActor() =
    DefaultActor<Command, Event> { command -> execute(command) }