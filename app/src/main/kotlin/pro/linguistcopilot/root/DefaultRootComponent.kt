package pro.linguistcopilot.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.pop
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.feature.auth.AuthComponent
import pro.linguistcopilot.feature.bookDescription.BookDescriptionComponent
import pro.linguistcopilot.feature.bookDownload.BookDownloadComponent
import pro.linguistcopilot.feature.bookReader.BookReaderComponent
import pro.linguistcopilot.feature.bookSearch.BookSearchComponent
import pro.linguistcopilot.feature.content.ContentComponent
import pro.linguistcopilot.feature.onboarding.OnboardingComponent

class DefaultRootComponent @AssistedInject constructor(
    private val onboardingFactory: OnboardingComponent.Factory,
    private val authFactory: AuthComponent.Factory,
    private val contentFactory: ContentComponent.Factory,
    private val bookDownloadFactory: BookDownloadComponent.Factory,
    private val bookDescriptionFactory: BookDescriptionComponent.Factory,
    private val bookReaderFactory: BookReaderComponent.Factory,
    private val bookSearchFactory: BookSearchComponent.Factory,
    @Assisted componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Content,
            serializer = Config.serializer(),
            handleBackButton = true,
            childFactory = ::child,
        )

    private fun child(config: Config, context: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Onboarding -> RootComponent.Child.Onboarding(onboardingComponent(context))
            is Config.Auth -> RootComponent.Child.Auth(authComponent(context))
            is Config.Content -> RootComponent.Child.Content(contentComponent(context))
            is Config.BookDownload -> RootComponent.Child.BookDownload(bookDownloadComponent(context))
            is Config.BookDescription -> RootComponent.Child.BookDescription(
                bookDescriptionComponent(context, config.bookId)
            )

            is Config.BookReader -> RootComponent.Child.BookReader(bookReaderComponent(context, config.bookId))
            is Config.BookSearch -> RootComponent.Child.BookSearch(bookSearchComponent(context))
        }

    private fun bookSearchComponent(context: ComponentContext): BookSearchComponent =
        bookSearchFactory(
            componentContext = context,
            onCloseBookSearch = {
                navigation.pop()
            },
            onOpenBookDescription = { bookId ->
                navigation.push(Config.BookDescription(bookId))
            }
        )

    private fun bookReaderComponent(context: ComponentContext, bookId: String): BookReaderComponent =
        bookReaderFactory(
            componentContext = context,
            bookId = bookId,
            onCloseBookReader = {
                navigation.pop()
            }
        )

    private fun bookDescriptionComponent(
        context: ComponentContext,
        bookId: String
    ): BookDescriptionComponent =
        bookDescriptionFactory(
            componentContext = context,
            bookId = bookId,
            onCloseBookDescription = {
                navigation.pop()
            },
            onOpenBookReader = { bookId ->
                navigation.push(Config.BookReader(bookId = bookId))
            }
        )

    private fun bookDownloadComponent(context: ComponentContext): BookDownloadComponent =
        bookDownloadFactory(
            componentContext = context,
            onCloseBookDownload = {
                navigation.pop()
            }
        )

    private fun contentComponent(context: ComponentContext): ContentComponent = contentFactory(
        componentContext = context,
        onNavigateToAuth = {
            navigation.replaceAll(Config.Auth)
        },
        onBookDownload = {
            navigation.push(Config.BookDownload)
        },
        onOpenBookDescription = { bookId ->
            navigation.push(Config.BookDescription(bookId))
        },
        onOpenBookSearch = {
            navigation.push(Config.BookSearch)
        }
    )

    private fun authComponent(context: ComponentContext): AuthComponent =
        authFactory(
            componentContext = context,
            onCloseAuth = {
                navigation.replaceAll(Config.Content)
            }
        )

    private fun onboardingComponent(context: ComponentContext): OnboardingComponent =
        onboardingFactory(
            componentContext = context,
            onCloseOnboarding = {
                navigation.replaceAll(Config.Auth)
            },
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Onboarding : Config

        @Serializable
        data object Auth : Config

        @Serializable
        data object Content : Config

        @Serializable
        data object BookDownload : Config

        @Serializable
        data class BookDescription(val bookId: String) : Config

        @Serializable
        data class BookReader(val bookId: String) : Config

        @Serializable
        data object BookSearch : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}