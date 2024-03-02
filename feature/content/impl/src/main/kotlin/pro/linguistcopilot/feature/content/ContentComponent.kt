package pro.linguistcopilot.feature.content

import com.arkivanov.decompose.ComponentContext


interface ContentComponent {
    val onNavigateToAuth: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToAuth: () -> Unit
        ): ContentComponent
    }
}