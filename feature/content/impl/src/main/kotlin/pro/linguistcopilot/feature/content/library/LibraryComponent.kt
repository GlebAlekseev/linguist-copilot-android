package pro.linguistcopilot.feature.content.library

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface LibraryComponent {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): LibraryComponent
    }
}