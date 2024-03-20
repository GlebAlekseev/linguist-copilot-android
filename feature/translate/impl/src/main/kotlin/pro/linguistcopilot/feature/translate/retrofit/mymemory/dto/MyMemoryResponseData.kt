package pro.linguistcopilot.feature.translate.retrofit.mymemory.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyMemoryResponseData(
    val match: Float = 0f,
    val translatedText: String = "",
    val detectedLanguage: String? = null
)
