package pro.linguistcopilot.feature.content.library

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.feature.content.library.externalSources.ExternalSourcesComponent
import pro.linguistcopilot.feature.content.library.mySources.MySourcesComponent

@Stable
interface LibraryComponent {
    @OptIn(ExperimentalDecomposeApi::class)
    val libraryPages: Value<ChildPages<*, Page>>
    val selectedIndex: Value<Int>
    val onSelectMySources: () -> Unit
    val onSelectExternalSources: () -> Unit

    sealed class Page {
        class MySources(val mySourcesComponent: MySourcesComponent) : Page()
        class ExternalSources(val externalSourcesComponent: ExternalSourcesComponent) : Page()
    }

    fun selectPageByIndex(index: Int)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onBookDownload: () -> Unit
        ): LibraryComponent
    }
}