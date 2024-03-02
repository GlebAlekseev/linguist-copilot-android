package pro.linguistcopilot.feature.content.library.external_sources

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface ExternalSourcesComponent {

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): ExternalSourcesComponent
    }
}