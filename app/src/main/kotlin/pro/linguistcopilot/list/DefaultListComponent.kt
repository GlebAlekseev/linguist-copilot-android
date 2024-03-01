package pro.linguistcopilot.list

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import pro.linguistcopilot.Item

class DefaultListComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted private val onItemSelected: (id: String) -> Unit,
) : ListComponent, ComponentContext by componentContext {

    override val items: Value<List<Item>> = MutableValue(List(10) { itemId ->
        Item(id = itemId.toString(), text = "text $itemId", title = "title $itemId")
    })

    override fun onItemClicked(id: String) {
        onItemSelected(id)
    }

    @AssistedFactory
    interface Factory : ListComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onItemSelected: (id: String) -> Unit,
        ): DefaultListComponent
    }
}
