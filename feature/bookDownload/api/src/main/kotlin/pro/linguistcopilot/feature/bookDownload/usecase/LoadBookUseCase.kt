package pro.linguistcopilot.feature.bookDownload.usecase

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import pro.linguistcopilot.core.utils.UseCase2
import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.repository.BookItemRepository
import pro.linguistcopilot.feature.bookDownload.controller.LocalBookProcessingController
import pro.linguistcopilot.feature.bookDownload.entity.ProgressResult
import javax.inject.Inject

class LoadBookUseCase @Inject constructor(
    private val bookItemRepository: BookItemRepository,
    private val localBookProcessingController: LocalBookProcessingController
) : UseCase2<LoadBookUseCase.Params, Flow<ProgressResult>>() {
    override fun execute(params: Params): Flow<ProgressResult> {
        return flow {
            emit(ProgressResult(progress = 0))
            val isSupportedExt = localBookProcessingController.isSupportedExt(params.uri)
            if (!isSupportedExt) {
                emit(
                    ProgressResult(
                        progress = 100,
                        toastMessage = "Расширение ${
                            params.uri.split(".").last()
                        } не поддерживается"
                    )
                )
                return@flow
            }
            val hash = localBookProcessingController.getHash(params.uri)
            val newTemporaryBookItem = BookItem(
                title = params.uri.split("/").lastOrNull()?.takeLast(6) ?: "Unknown name",
                uri = params.uri,
                hash = hash
            )
            val isExist = bookItemRepository.isExist(hash)
            var temporaryBookItem = bookItemRepository.addTemporaryBookItem(newTemporaryBookItem)
            emit(ProgressResult(progress = (1..5).random()))
            if (isExist) {
                bookItemRepository.removeTemporaryBookItemById(temporaryBookItem.id)
                emit(
                    ProgressResult(
                        progress = 100,
                        toastMessage = "Книга уже была ранее загружена"
                    )
                )
                return@flow
            }
            emit(ProgressResult(progress = (6..10).random()))
            val isValidExt = localBookProcessingController.isValidExt(params.uri)
            emit(ProgressResult(progress = (11..15).random()))
            if (!isValidExt) {
                temporaryBookItem = temporaryBookItem.copy(
                    isBad = true,
                    errorMessage = "Неверное расширение"
                )
                bookItemRepository.removeTemporaryBookItemAndAddBookItem(temporaryBookItem)
                emit(ProgressResult(progress = 100))
                return@flow
            }
            val isValidSize = localBookProcessingController.isValidSize(params.uri)
            emit(ProgressResult(progress = (16..20).random()))
            if (!isValidSize) {
                temporaryBookItem = temporaryBookItem.copy(
                    isBad = true,
                    errorMessage = "Размер превысил N"
                )
                bookItemRepository.removeTemporaryBookItemAndAddBookItem(temporaryBookItem)
                emit(ProgressResult(progress = 100))
                return@flow
            }
            val isValidMimeType = localBookProcessingController.isValidMimeType(params.uri)
            emit(ProgressResult(progress = (21..25).random()))
            if (!isValidMimeType) {
                temporaryBookItem = temporaryBookItem.copy(
                    isBad = true,
                    errorMessage = "Файл испорчен"
                )
                bookItemRepository.removeTemporaryBookItemAndAddBookItem(temporaryBookItem)
                emit(ProgressResult(progress = 100))
                return@flow
            }
            bookItemRepository.updateTemporaryBookItem(temporaryBookItem.copy(isValid = true))
            emit(ProgressResult(progress = (26..30).random()))
            val epubUri = localBookProcessingController.convert2epub(params.uri)
            emit(ProgressResult(progress = (31..55).random()))
            if (epubUri != null) {
                bookItemRepository.updateTemporaryBookItem(temporaryBookItem.copy(epubUri = epubUri))
            }
            emit(ProgressResult(progress = (56..60).random()))
            val pdfUri = localBookProcessingController.convert2pdf(params.uri)
            emit(ProgressResult(progress = (61..85).random()))
            if (pdfUri != null) {
                bookItemRepository.updateTemporaryBookItem(temporaryBookItem.copy(pdfUri = pdfUri))
            }
            emit(ProgressResult(progress = (86..90).random()))
            if (pdfUri != null || epubUri != null) {
                val metaInfo = localBookProcessingController.getMetaInfo(epubUri, pdfUri)
                emit(ProgressResult(progress = 95))
                bookItemRepository.updateTemporaryBookItem(temporaryBookItem.copy(metaInfo = metaInfo))
            } else {
                temporaryBookItem = temporaryBookItem.copy(
                    isBad = true,
                    errorMessage = "Файл не поддерживается"
                )
                emit(ProgressResult(progress = 95))
            }
            bookItemRepository.removeTemporaryBookItemAndAddBookItem(temporaryBookItem)
            emit(ProgressResult(progress = 100))
            return@flow
        }.flowOn(Dispatchers.IO)
    }

    @JvmInline
    value class Params(val uri: String) : UseCase2.Params
}
