package pro.linguistcopilot.feature.bookDownload

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.router.slot.SlotNavigation
import com.arkivanov.decompose.router.slot.activate
import com.arkivanov.decompose.router.slot.childSlot
import com.arkivanov.decompose.router.slot.dismiss
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.instancekeeper.getOrCreate
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import kotlinx.serialization.Serializable
import pro.linguistcopilot.core.utils.elmslie_decompose.RetainedElmStore
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadActor
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadEvent
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadReducer
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadState
import pro.linguistcopilot.feature.bookDownload.loadVariantSheet.DefaultLoadVariantSheetComponent
import pro.linguistcopilot.feature.bookDownload.loadVariantSheet.LoadVariantSheetComponent

class DefaultBookDownloadComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("onCloseBookDownload") override val onCloseBookDownload: () -> Unit,
    private val bookDownloadReducer: BookDownloadReducer,
    private val bookDownloadActor: BookDownloadActor,
) : BookDownloadComponent, ComponentContext by componentContext {
    private val retainedElmStore = instanceKeeper.getOrCreate {
        RetainedElmStore(
            startEvent = BookDownloadEvent.Ui.Start,
            initialState = BookDownloadState.Init,
            reducer = bookDownloadReducer,
            actor = bookDownloadActor
        ).also { it.start() }
    }

    override val currentState get() = retainedElmStore.currentState
    override val states get() = retainedElmStore.states()
    override val effects get() = retainedElmStore.effects()
    override fun accept(testEvent: BookDownloadEvent) = retainedElmStore.accept(testEvent)

    private val loadVariantSheetNavigation = SlotNavigation<LoadVariantSheetConfig>()

    override val loadVariantSheet: Value<ChildSlot<*, LoadVariantSheetComponent>> =
        childSlot(
            source = loadVariantSheetNavigation,
            serializer = LoadVariantSheetConfig.serializer(),
            handleBackButton = true,
            key = "loadVariantSheetChildSlot"
        ) { config, childComponentContext ->
            DefaultLoadVariantSheetComponent(
                componentContext = childComponentContext,
                onLoadUri = { uri -> accept(BookDownloadEvent.Ui.Load(uri)) },
                onDismissRequest = loadVariantSheetNavigation::dismiss
            )
        }

    override fun showLoadVariant() {
        loadVariantSheetNavigation.activate(LoadVariantSheetConfig)
    }

    @Serializable
    private object LoadVariantSheetConfig

    @AssistedFactory
    interface Factory : BookDownloadComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("onCloseBookDownload") onCloseBookDownload: () -> Unit
        ): DefaultBookDownloadComponent
    }
}