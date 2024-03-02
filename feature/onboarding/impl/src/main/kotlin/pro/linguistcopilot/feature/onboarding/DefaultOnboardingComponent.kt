package pro.linguistcopilot.feature.onboarding

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.ExperimentalDecomposeApi
import com.arkivanov.decompose.router.pages.ChildPages
import com.arkivanov.decompose.router.pages.Pages
import com.arkivanov.decompose.router.pages.PagesNavigation
import com.arkivanov.decompose.router.pages.childPages
import com.arkivanov.decompose.router.pages.select
import com.arkivanov.decompose.value.Value
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.feature.onboarding.page.OnboardingPageComponent

@OptIn(ExperimentalDecomposeApi::class)
class DefaultOnboardingComponent @AssistedInject constructor(
    private val onboardingPageFactory: OnboardingPageComponent.Factory,
    @Assisted componentContext: ComponentContext,
    @Assisted onCloseOnboarding: () -> Unit
) : OnboardingComponent, ComponentContext by componentContext {

    private val navigation = PagesNavigation<Config>()

    override val onboardingPages: Value<ChildPages<*, OnboardingPageComponent>> =
        childPages(
            source = navigation,
            serializer = Config.serializer(),
            initialPages = {
                Pages(
                    items = listOf(
                        Config.Welcome,
                        Config.TranslateFeature,
                        Config.VocabularyFeature
                    ),
                    selectedIndex = 0
                )
            },
            handleBackButton = true,
            childFactory = ::child
        )

    private fun child(config: Config, context: ComponentContext): OnboardingPageComponent =
        when (config) {
            Config.VocabularyFeature -> onboardingPageFactory.invoke(context, "$config", 2)
            Config.TranslateFeature -> onboardingPageFactory.invoke(context, "$config", 1)
            Config.Welcome -> onboardingPageFactory.invoke(context, "$config", 0)
        }

    override fun selectPageByIndex(index: Int) {
        navigation.select(index = index)
    }

    @Serializable
    private sealed interface Config {
        @Serializable
        data object Welcome : Config

        @Serializable
        data object TranslateFeature : Config

        @Serializable
        data object VocabularyFeature : Config
    }

    @AssistedFactory
    interface Factory : OnboardingComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            onCloseOnboarding: () -> Unit
        ): DefaultOnboardingComponent
    }
}