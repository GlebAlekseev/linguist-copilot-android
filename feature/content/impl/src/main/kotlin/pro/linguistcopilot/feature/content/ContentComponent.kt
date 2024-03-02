package pro.linguistcopilot.feature.content

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.value.Value
import pro.linguistcopilot.feature.content.library.LibraryComponent
import pro.linguistcopilot.feature.content.overview.OverviewComponent
import pro.linguistcopilot.feature.content.profile.ProfileComponent

@OptIn(ExperimentalDecomposeApi::class)
@Stable
interface ContentComponent {
    val onNavigateToAuth: () -> Unit
    val selectedIndex: Value<Int>
    val contentPages: Value<ChildPages<*, Page>>
    val onSelectLibrary: () -> Unit
    val onSelectOverview: () -> Unit
    val onSelectProfile: () -> Unit

    sealed class Page {
        class Library(val libraryComponent: LibraryComponent) : Page()
        class Overview(val overviewComponent: OverviewComponent) : Page()
        class Profile(val profileComponent: ProfileComponent) : Page()
    }

    fun selectPageByIndex(index: Int)
    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onNavigateToAuth: () -> Unit,
            onBookDownload: () -> Unit,
            onOpenBookDescription: () -> Unit,
        ): ContentComponent
    }
}