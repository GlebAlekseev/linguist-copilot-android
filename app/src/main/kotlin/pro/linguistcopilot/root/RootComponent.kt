package pro.linguistcopilot.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.details.DetailsComponent
import pro.linguistcopilot.list.ListComponent

interface RootComponent {
    val stack: Value<ChildStack<*, RootState>>

    sealed class RootState {
        class ListState(val listComponent: ListComponent) : RootState()
        class DetailsState(val detailsComponent: DetailsComponent) : RootState()
    }

    fun interface Factory {
        operator fun invoke(componentContext: ComponentContext): RootComponent
    }
}