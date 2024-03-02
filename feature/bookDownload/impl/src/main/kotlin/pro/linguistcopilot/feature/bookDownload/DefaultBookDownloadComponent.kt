package pro.linguistcopilot.feature.auth

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultBookDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseBookDownload") override val onCloseBookDownload: () -> Unit
) : BookDownloadComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : BookDownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseBookDownload") onCloseBookDownload: () -> Unit
        ): DefaultBookDownloadComponent
    }
}