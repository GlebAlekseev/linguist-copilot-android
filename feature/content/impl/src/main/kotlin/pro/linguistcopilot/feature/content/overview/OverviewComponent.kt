package pro.linguistcopilot.feature.content.overview

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface OverviewComponent {
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): OverviewComponent
    }
}