package pro.linguistcopilot.feature.translate.retrofit.mymemory.dto

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
data class MyMemoryTranslationResponse(
    @SerializedName("exception_code") val exceptionCode: Int? = null,
    val mtLangSupported: Boolean? = false,
    val quotaFinished: Boolean? = false,
    val responderId: String? = "",
    val responseData: MyMemoryResponseData? = null,
    val responseDetails: String = "",
    val responseStatus: Int = 0
)
