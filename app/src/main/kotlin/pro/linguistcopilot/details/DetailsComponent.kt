package pro.linguistcopilot.details

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.Item

interface DetailsComponent {

    val item: Value<Item>

    fun onCloseClicked()

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            itemId: String,
            onFinished: () -> Unit,
        ): DetailsComponent
    }
}


