package pro.linguistcopilot.feature.translate.retrofit.mymemory.dto

import kotlinx.serialization.Serializable

@Serializable
data class MyMemoryTranslation(
    val id: String = "",
    val match: Int = 0,
    val quality: String = "",
    val segment: String = "",
    val source: String = "",
    val subject: String = "",
    val target: String = "",
    val translation: String = ""
)
