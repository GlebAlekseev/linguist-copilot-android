package pro.linguistcopilot.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.MutableValue
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import pro.linguistcopilot.Item

class DefaultDetailsComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted itemId: String,
    @Assisted private val onFinished: () -> Unit,
) : DetailsComponent, ComponentContext by componentContext {

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
