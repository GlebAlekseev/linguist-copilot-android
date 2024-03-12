package pro.linguistcopilot.feature.content.library

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
import pro.linguistcopilot.feature.content.library.externalSources.ExternalSourcesComponent
import pro.linguistcopilot.feature.content.library.mySources.MySourcesComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultLibraryComponent @AssistedInject constructor(
    private val mySourcesFactory: MySourcesComponent.Factory,
    private val externalSourcesFactory: ExternalSourcesComponent.Factory,
    @Assisted componentContext: ComponentContext,
    @Assisted("onBookDownload") private val onBookDownload: () -> Unit,
    @Assisted("onOpenBookDescription") private val onOpenBookDescription: (String) -> Unit
) : LibraryComponent, ComponentContext by componentContext {
    private val navigation = PagesNavigation<DefaultLibraryComponent.Config>()

    override val libraryPages: Value<ChildPages<*, LibraryComponent.Page>> =
        childPages(
            source = navigation,
            serializer = Config.serializer(),
            initialPages = {
                Pages(
                    items = listOf(
                        Config.MySources,
                        Config.ExternalSources,
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
    override val selectedIndex: Value<Int> get() = libraryPages.map { it.selectedIndex }

    override val onSelectMySources: () -> Unit = {
        navigation.select(0)
    }
    override val onSelectExternalSources: () -> Unit = {
        navigation.select(1)
    }

    override fun selectPageByIndex(index: Int) {
        navigation.select(index = index)
    }

    private fun child(
        config: Config,
        context: ComponentContext
    ): LibraryComponent.Page =
        when (config) {
            Config.ExternalSources -> LibraryComponent.Page.ExternalSources(
                externalSourcesComponent(
                    context
                )
            )

            Config.MySources -> LibraryComponent.Page.MySources(mySourcesComponent(context))
        }

    private fun mySourcesComponent(context: ComponentContext): MySourcesComponent =
        mySourcesFactory(
            componentContext = context,
            onBookDownload = onBookDownload,
            onOpenBookDescription = onOpenBookDescription
        )

    private fun externalSourcesComponent(context: ComponentContext): ExternalSourcesComponent =
        externalSourcesFactory(
            componentContext = context
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object MySources : Config

        @Serializable
        data object ExternalSources : Config
    }

    @AssistedFactory
    interface Factory : LibraryComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onBookDownload") onBookDownload: () -> Unit,
            @Assisted("onOpenBookDescription") onOpenBookDescription: (String) -> Unit
        ): DefaultLibraryComponent
    }
}