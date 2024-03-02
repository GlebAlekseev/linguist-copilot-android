package pro.linguistcopilot.feature.auth

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext


@Stable
interface AuthComponent {
    val onCloseAuth: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseAuth: () -> Unit
        ): AuthComponent
    }
}