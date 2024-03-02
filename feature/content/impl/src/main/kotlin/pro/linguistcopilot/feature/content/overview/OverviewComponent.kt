package pro.linguistcopilot.feature.content.overview

import com.arkivanov.decompose.ComponentContext

interface OverviewComponent {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): OverviewComponent
    }
}