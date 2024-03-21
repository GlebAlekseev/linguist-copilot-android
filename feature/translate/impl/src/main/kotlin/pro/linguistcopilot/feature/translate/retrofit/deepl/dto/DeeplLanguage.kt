package pro.linguistcopilot.feature.translate.retrofit.deepl.dto

import kotlinx.serialization.Serializable

@Serializable
data class DeeplLanguage(
    val language: String = "",
    val name: String = ""
)
