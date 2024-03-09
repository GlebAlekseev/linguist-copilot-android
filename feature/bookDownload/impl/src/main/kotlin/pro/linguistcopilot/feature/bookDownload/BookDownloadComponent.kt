package pro.linguistcopilot.feature.bookDownload

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.router.slot.ChildSlot
import com.arkivanov.decompose.value.Value
import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadEffect
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadEvent
import pro.linguistcopilot.feature.bookDownload.elm.BookDownloadState
import pro.linguistcopilot.feature.bookDownload.loadVariantSheet.LoadVariantSheetComponent


@Stable
interface BookDownloadComponent {
    val currentState: BookDownloadState
    val states: Flow<BookDownloadState>
    val effects: Flow<BookDownloadEffect>

    val onCloseBookDownload: () -> Unit

    val loadVariantSheet: Value<ChildSlot<*, LoadVariantSheetComponent>>
    fun showLoadVariant()
    fun accept(testEvent: BookDownloadEvent)

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            onCloseBookDownload: () -> Unit
        ): BookDownloadComponent
    }
}