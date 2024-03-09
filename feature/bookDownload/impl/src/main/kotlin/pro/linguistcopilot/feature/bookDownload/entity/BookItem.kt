package pro.linguistcopilot.feature.bookDownload.entity

import androidx.compose.runtime.Immutable
import java.util.Date

@Immutable
data class BookItem(
    val title: String,
    val date: Date
)