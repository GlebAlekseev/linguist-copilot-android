package pro.linguistcopilot.feature.textProcessing.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.textProcessing.controller.OpenNLPTextProcessingControllerImpl
import pro.linguistcopilot.feature.textProcessing.controller.TextProcessingController


@Module
interface TextProcessingModule {

    @Binds
    fun textProcessingController(impl: OpenNLPTextProcessingControllerImpl): TextProcessingController
}
