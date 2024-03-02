package pro.linguistcopilot.feature.content

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.children.ChildNavState
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import com.arkivanov.decompose.value.operator.map
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.feature.content.library.LibraryComponent
import pro.linguistcopilot.feature.content.overview.OverviewComponent
import pro.linguistcopilot.feature.content.profile.ProfileComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultContentComponent @AssistedInject constructor(
    private val libraryFactory: LibraryComponent.Factory,
    private val overviewFactory: OverviewComponent.Factory,
    private val profileFactory: ProfileComponent.Factory,
    @Assisted componentContext: ComponentContext,
    @Assisted("onNavigateToAuth") override val onNavigateToAuth: () -> Unit,
    @Assisted("onBookDownload") private val onBookDownload: () -> Unit,
) : ContentComponent, ComponentContext by componentContext {
    private val navigation = PagesNavigation<Config>()
    override val selectedIndex: Value<Int> get() = contentPages.map { it.selectedIndex }

    override val contentPages: Value<ChildPages<*, ContentComponent.Page>> =
        childPages(
            source = navigation,
            serializer = Config.serializer(),
            initialPages = {
                Pages(
                    items = listOf(
                        Config.Library,
                        Config.Overview,
                        Config.Profile,
                    ),
                    selectedIndex = 0
                )
            },
            pageStatus = { index, pages ->
                when (index) {
                    pages.selectedIndex -> ChildNavState.Status.ACTIVE
                    else -> ChildNavState.Status.INACTIVE
                }
            },
            handleBackButton = false,
            childFactory = ::child
        )

    override val onSelectLibrary: () -> Unit = {
        navigation.select(0)
    }
    override val onSelectOverview: () -> Unit = {
        navigation.select(1)
    }
    override val onSelectProfile: () -> Unit = {
        navigation.select(2)
    }


    private fun child(config: Config, context: ComponentContext): ContentComponent.Page =
        when (config) {
            Config.Library -> ContentComponent.Page.Library(libraryComponent(context))
            Config.Overview -> ContentComponent.Page.Overview(overviewComponent(context))
            Config.Profile -> ContentComponent.Page.Profile(profileComponent(context))
        }

    private fun libraryComponent(context: ComponentContext): LibraryComponent = libraryFactory(
        componentContext = context,
        onBookDownload = onBookDownload
    )

    private fun overviewComponent(context: ComponentContext): OverviewComponent = overviewFactory(
        componentContext = context
    )

    private fun profileComponent(context: ComponentContext): ProfileComponent = profileFactory(
        componentContext = context
    )

    override fun selectPageByIndex(index: Int) {
        navigation.select(index = index)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Library : Config

        @Serializable
        data object Overview : Config

        @Serializable
        data object Profile : Config
    }

    @AssistedFactory
    interface Factory : ContentComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onNavigateToAuth") onNavigateToAuth: () -> Unit,
            @Assisted("onBookDownload") onBookDownload: () -> Unit
        ): DefaultContentComponent
    }
}