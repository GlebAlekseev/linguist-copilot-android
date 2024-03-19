package pro.linguistcopilot.feature.translate.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.translate.controller.MLKitTextTranslationControllerImpl
import pro.linguistcopilot.feature.translate.controller.TextTranslationController

@Module
interface TranslateModule {

    @Binds
    fun textTranslationController(impl: MLKitTextTranslationControllerImpl): TextTranslationController
}
