package pro.linguistcopilot.feature.translate.retrofit.deepl.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslationResponse(
    val translations: List<DeeplTranslation> = listOf()
)
