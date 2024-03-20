package pro.linguistcopilot.feature.translate.entity

import kotlinx.serialization.Serializable

@Serializable
sealed class TranslationEngineConfig {
    abstract val title: String

    @Serializable
    data class Deepl(
        override val title: String,
        val apiKey: String,
        val isFree: Boolean
    ) : TranslationEngineConfig()

    @Serializable
    data class MyMemory(
        override val title: String,
        val email: String
    ) : TranslationEngineConfig()

    @Serializable
    data class MlKit(
        override val title: String,
    ) : TranslationEngineConfig()
}
