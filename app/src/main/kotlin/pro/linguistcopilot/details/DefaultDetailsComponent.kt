package pro.linguistcopilot.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import pro.linguistcopilot.Item
import vivid.money.elmslie.core.store.dsl_reducer.ScreenDslReducer
import vivid.money.elmslie.coroutines.Actor
import vivid.money.elmslie.coroutines.ElmStoreCompat


data class TestState(val text: String)
sealed class TestCommand {
    data class Send(val id: Int) : TestCommand()
}

sealed class TestEffect {
    data class ShowError(val text: String) : TestEffect()
}

sealed class TestEvent {
    sealed class Ui : TestEvent() {
        data object Start : Ui()
    }

    sealed class Internal : TestEvent() {
        data object FinishLoading : Internal()
        data object ErrorLoading : Internal()
    }
}

object TestReducer :
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
            TestEvent.Ui.Start -> println("TestEvent.Ui.Start")
        }
    }
}

class TestActor : Actor<TestCommand, TestEvent.Internal> {
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
    @Assisted itemId: String,
    @Assisted private val onFinished: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

    private val store = ElmStoreCompat(
        startEvent = TestEvent.Ui.Start,
        initialState = TestState("init"),
        reducer = TestReducer,
        actor = TestActor()
    ).start()
    override val currentState: TestState
        get() = store.currentState

    override val states get() = store.states()
    override val effects get() = store.effects()
    override fun accept(testEvent: TestEvent) = store.accept(testEvent)

    override val item: Value<Item> =
        MutableValue(Item(id = itemId, text = "text $itemId", title = "title $itemId"))

    override fun onCloseClicked() {
        onFinished()
    }

    @AssistedFactory
    interface Factory : DetailsComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            itemId: String,
            onFinished: () -> Unit,
        ): DefaultDetailsComponent
    }
}
