package pro.linguistcopilot.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.replaceAll
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.feature.auth.AuthComponent
import pro.linguistcopilot.feature.content.ContentComponent
import pro.linguistcopilot.feature.onboarding.OnboardingComponent

class DefaultRootComponent @AssistedInject constructor(
    private val onboardingFactory: OnboardingComponent.Factory,
    private val authFactory: AuthComponent.Factory,
    private val contentFactory: ContentComponent.Factory,
    @Assisted componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {
    private val navigation = StackNavigation<Config>()
    override val stack: Value<ChildStack<*, RootComponent.Child>> =
        childStack(
            source = navigation,
            initialConfiguration = Config.Onboarding,
            serializer = Config.serializer(),
            handleBackButton = false,
            childFactory = ::child,
        )

    private fun child(config: Config, context: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Onboarding -> RootComponent.Child.Onboarding(onboardingComponent(context))
            is Config.Auth -> RootComponent.Child.Auth(authComponent(context))
            is Config.Content -> RootComponent.Child.Content(contentComponent(context))
        }

    private fun contentComponent(context: ComponentContext): ContentComponent = contentFactory(
        componentContext = context,
        onNavigateToAuth = {
            navigation.replaceAll(Config.Auth)
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
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}