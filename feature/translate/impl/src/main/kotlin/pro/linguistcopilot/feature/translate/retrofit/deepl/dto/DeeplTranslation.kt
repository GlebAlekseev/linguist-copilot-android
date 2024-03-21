package pro.linguistcopilot.feature.translate.retrofit.deepl.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class DeeplTranslation(
    @SerializedName("detected_source_language") val detectedSourceLanguage: String = "",
    val text: String = ""
)
