package pro.linguistcopilot.feature.content.profile

import com.arkivanov.decompose.ComponentContext

interface ProfileComponent {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): ProfileComponent
    }
}