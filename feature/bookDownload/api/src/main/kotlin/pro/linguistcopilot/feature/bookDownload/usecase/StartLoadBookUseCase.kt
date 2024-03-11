package pro.linguistcopilot.feature.bookDownload.usecase

import pro.linguistcopilot.core.utils.UseCase
import pro.linguistcopilot.feature.bookDownload.controller.LoadBookServiceController
import javax.inject.Inject

class StartLoadBookUseCase @Inject constructor(
    private val loadBookServiceController: LoadBookServiceController
) : UseCase<StartLoadBookUseCase.Params, Unit>() {
    override suspend fun execute(params: Params) {
        loadBookServiceController.start(params.uri)
    }

    @JvmInline
    value class Params(val uri: String) : UseCase.Params
}
