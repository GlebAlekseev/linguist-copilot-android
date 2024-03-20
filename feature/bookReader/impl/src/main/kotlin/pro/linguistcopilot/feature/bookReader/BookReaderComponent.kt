package pro.linguistcopilot.feature.bookReader

import androidx.compose.runtime.Stable
import com.arkivanov.decompose.ComponentContext
import pro.linguistcopilot.feature.textProcessing.controller.TextProcessingController
import pro.linguistcopilot.feature.translate.usecase.TranslateTextUseCase


@Stable
interface BookReaderComponent {
    val onCloseBookReader: () -> Unit
    val textProcessingController: TextProcessingController
    val translateTextUseCase: TranslateTextUseCase

    fun interface Factory {
        operator fun invoke(
            componentContext: ComponentContext,
            bookId: String,
            onCloseBookReader: () -> Unit,
        ): BookReaderComponent
    }
}