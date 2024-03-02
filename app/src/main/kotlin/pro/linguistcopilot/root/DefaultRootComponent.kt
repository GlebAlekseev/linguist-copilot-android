package pro.linguistcopilot.root

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.feature.onboarding.OnboardingComponent

class DefaultRootComponent @AssistedInject constructor(
    private val onboardingFactory: OnboardingComponent.Factory,
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
        }

    private fun onboardingComponent(context: ComponentContext): OnboardingComponent =
        onboardingFactory(
            componentContext = context,
            onCloseOnboarding = {
                //navigation.push(Config.Auth)
            },
        )

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Onboarding : Config
    }

    @AssistedFactory
    interface Factory : RootComponent.Factory {
        override fun invoke(componentContext: ComponentContext): DefaultRootComponent
    }
}