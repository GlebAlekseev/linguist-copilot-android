package pro.linguistcopilot.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.Item

interface DetailsComponent {
    val currentState : TestState
    val states: Flow<TestState>
    val effects: Flow<TestEffect>
    fun accept(testEvent: TestEvent)

    val item: Value<Item>

    fun onCloseClicked()
    fun navigateTo()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            itemId: String,
            onFinished: () -> Unit,
        ): DetailsComponent
    }
}


