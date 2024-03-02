package pro.linguistcopilot.feature.content.library

import com.arkivanov.decompose.ComponentContext

interface LibraryComponent {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): LibraryComponent
    }
}