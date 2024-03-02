package pro.linguistcopilot.feature.auth

import com.arkivanov.decompose.ComponentContext


interface AuthComponent {

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseAuth: () -> Unit
        ): AuthComponent
    }
}