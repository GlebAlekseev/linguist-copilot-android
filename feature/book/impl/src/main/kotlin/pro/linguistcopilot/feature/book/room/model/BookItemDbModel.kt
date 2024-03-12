package pro.linguistcopilot.feature.book.room.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import pro.linguistcopilot.feature.book.entity.MetaInfo
import java.util.Date

@Entity
data class BookItemDbModel(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: String,
    @ColumnInfo(name = "title")
    val title: String,
    @ColumnInfo(name = "uri")
    val uri: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Date,
    @ColumnInfo(name = "is_bad")
    val isBad: Boolean,
    @ColumnInfo(name = "is_valid")
    val isValid: Boolean,
    @ColumnInfo(name = "error_message")
    val errorMessage: String,
    @ColumnInfo(name = "epub_uri")
    val epubUri: String,
    @ColumnInfo(name = "pdf_uri")
    val pdfUri: String,
    @ColumnInfo(name = "meta_info")
    val metaInfo: MetaInfo?,
    @ColumnInfo(name = "hash")
    val hash: String
)
