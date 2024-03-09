package pro.linguistcopilot.feature.bookDownload.loadVariantSheet

import android.net.Uri
import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultLoadVariantSheetComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onDismissRequest") val onDismissRequest: () -> Unit,
    @Assisted("onLoadUri") val onLoadUri: (uri: Uri) -> Unit,
) : LoadVariantSheetComponent, ComponentContext by componentContext {

    override fun onDismissRequest() {
        onDismissRequest.invoke()
    }

    override fun onLoadUri(uri: Uri) {
        onLoadUri.invoke(uri)
    }

    @AssistedFactory
    interface Factory : LoadVariantSheetComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onDismissRequest") onDismissRequest: () -> Unit,
            @Assisted("onLoadUri") onLoadUri: (uri: Uri) -> Unit,
        ): DefaultLoadVariantSheetComponent
    }
}