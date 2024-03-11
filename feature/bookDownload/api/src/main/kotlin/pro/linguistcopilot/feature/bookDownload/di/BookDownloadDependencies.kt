package pro.linguistcopilot.feature.bookDownload.di

import pro.linguistcopilot.feature.bookDownload.usecase.LoadBookUseCase

interface BookDownloadDependencies {
    val loadBookUseCase: LoadBookUseCase
}

