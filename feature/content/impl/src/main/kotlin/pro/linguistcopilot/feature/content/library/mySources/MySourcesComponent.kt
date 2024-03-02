package pro.linguistcopilot.feature.content.library.mySources

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext

@Stable
interface MySourcesComponent {
    val onBookDownload: () -> Unit
    val onOpenBookDescription: () -> Unit

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBookDownload: () -> Unit,
            onOpenBookDescription: () -> Unit,
        ): MySourcesComponent
    }
}