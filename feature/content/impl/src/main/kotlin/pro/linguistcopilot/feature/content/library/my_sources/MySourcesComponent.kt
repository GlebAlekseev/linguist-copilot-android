package pro.linguistcopilot.feature.content.library.my_sources

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface MySourcesComponent {

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext
        ): MySourcesComponent
    }
}