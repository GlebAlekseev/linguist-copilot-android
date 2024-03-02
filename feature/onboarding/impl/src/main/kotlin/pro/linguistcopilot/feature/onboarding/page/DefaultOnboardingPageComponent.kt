package pro.linguistcopilot.feature.onboarding.page

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject


class DefaultOnboardingPageComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("message") override val message: String,
    @Assisted("index") override val index: Int,
    @Assisted("onCloseOnboarding") override val onCloseOnboarding: (() -> Unit)?,
) : OnboardingPageComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : OnboardingPageComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("message") message: String,
            @Assisted("index") index: Int,
            @Assisted("onCloseOnboarding") onCloseOnboarding: (() -> Unit)?
        ): DefaultOnboardingPageComponent
    }
}