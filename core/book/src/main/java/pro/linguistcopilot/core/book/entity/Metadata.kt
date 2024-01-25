package pro.linguistcopilot.core.book.entity

import android.net.Uri

data class Metadata(
    val title: String = "",
    val titles: List<String> = listOf(),
    val authors: List<String> = listOf(),
    val language: String = "",
    val dates: List<String> = listOf(),
    val publishers: List<String> = listOf(),
    val descriptions: List<String> = listOf(),
    val subjects: List<String> = listOf(),
    val coverImageUri: Uri? = null,
    val uri: Uri? = null,
    val format: String = "",
)