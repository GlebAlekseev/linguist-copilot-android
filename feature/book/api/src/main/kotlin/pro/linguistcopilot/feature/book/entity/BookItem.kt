package pro.linguistcopilot.feature.book.entity

import java.util.Date
import java.util.UUID

data class BookItem(
    val id: String = UUID.randomUUID().toString(),
    val title: String,
    val uri: String,
    val createdAt: Date = Date(),
    val isBad: Boolean = false,
    val isValid: Boolean = false,
    val errorMessage: String = "",
    val epubUri: String? = null,
    val pdfUri: String? = null,
    val metaInfo: MetaInfo? = null,
    val hash: String
)
