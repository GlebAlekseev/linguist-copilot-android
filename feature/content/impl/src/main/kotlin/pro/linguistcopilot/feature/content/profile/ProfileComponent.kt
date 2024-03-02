package pro.linguistcopilot.feature.content.profile

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface ProfileComponent {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): ProfileComponent
    }
}