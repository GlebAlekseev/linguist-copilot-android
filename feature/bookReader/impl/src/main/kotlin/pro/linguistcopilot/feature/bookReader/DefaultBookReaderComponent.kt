package pro.linguistcopilot.feature.bookReader

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import pro.linguistcopilot.feature.textProcessing.controller.TextProcessingController
import pro.linguistcopilot.feature.translate.controller.TextTranslationController

class DefaultBookReaderComponent @AssistedInject constructor(
    @Assisted componentContext: ComponentContext,
    @Assisted("bookId") private val bookId: String,
    @Assisted("onCloseBookReader") override val onCloseBookReader: () -> Unit,
    override val textProcessingController: TextProcessingController,
    override val textTranslationController: TextTranslationController
) : BookReaderComponent, ComponentContext by componentContext {

    @AssistedFactory
    interface Factory : BookReaderComponent.Factory {
        override fun invoke(
            componentContext: ComponentContext,
            @Assisted("bookId") bookId: String,
            @Assisted("onCloseBookReader") onCloseBookReader: () -> Unit,
        ): DefaultBookReaderComponent
    }
}