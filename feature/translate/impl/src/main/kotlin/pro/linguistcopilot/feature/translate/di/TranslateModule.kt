package pro.linguistcopilot.feature.translate.di

import dagger.Binds
import dagger.Module
import pro.linguistcopilot.feature.translate.controller.DeeplTextTranslationControllerImpl
import pro.linguistcopilot.feature.translate.controller.MLKitTextTranslationControllerImpl
import pro.linguistcopilot.feature.translate.controller.MyMemoryTextTranslationControllerImpl
import pro.linguistcopilot.feature.translate.controller.TextTranslationController
import pro.linguistcopilot.feature.translate.repository.TranslationEngineConfigRepository
import pro.linguistcopilot.feature.translate.repository.TranslationEngineConfigRepositoryImpl

@Module(includes = [RemoteModule::class])
interface TranslateModule {

    @Binds
    fun translationEngineConfigRepository(impl: TranslationEngineConfigRepositoryImpl): TranslationEngineConfigRepository

    @MlKitQualifier
    @Binds
    fun mLKitTextTranslationControllerImpl(impl: MLKitTextTranslationControllerImpl): TextTranslationController

    @MyMemoryQualifier
    @Binds
    fun myMemoryTextTranslationControllerImpl(impl: MyMemoryTextTranslationControllerImpl): TextTranslationController

    @DeeplQualifier
    @Binds
    fun deeplTextTranslationControllerImpl(impl: DeeplTextTranslationControllerImpl): TextTranslationController
}