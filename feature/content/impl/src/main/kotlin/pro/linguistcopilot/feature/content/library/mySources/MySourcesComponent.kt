package pro.linguistcopilot.feature.content.library.mySources

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesEffect
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesEvent
import pro.linguistcopilot.feature.content.library.mySources.elm.MySourcesState

@Stable
interface MySourcesComponent {
    val onBookDownload: () -> Unit
    val onOpenBookDescription: (String) -> Unit

    val currentState: MySourcesState
    val states: Flow<MySourcesState>
    val effects: Flow<MySourcesEffect>
    fun accept(event: MySourcesEvent)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBookDownload: () -> Unit,
            onOpenBookDescription: (String) -> Unit,
        ): MySourcesComponent
    }
}