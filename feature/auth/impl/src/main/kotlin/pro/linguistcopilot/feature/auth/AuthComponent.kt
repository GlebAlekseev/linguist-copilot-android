package pro.linguistcopilot.feature.auth

import com.arkivanov.decompose.ComponentContext


interface AuthComponent {
    val onCloseAuth: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseAuth: () -> Unit
        ): AuthComponent
    }
}