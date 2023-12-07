package pro.linguistcopilot.features.reader.domain

import androidx.annotation.IntDef

data class Book(
    val url: String,
    @BookType val bookType: Int,
    val createdAt: Long,
    val changedAt: Long
) {
    companion object {
        fun Book.bookInfo(): BookInfo {
            return BookInfo(
                title = "Title",
                author = Author(
                    name = "Alex",
                    surname = "Surlex"
                )
            )
        }

        const val UNSUPPORTED = 0
        const val EPUB = 1

        @Target(AnnotationTarget.VALUE_PARAMETER)
        @Retention(AnnotationRetention.SOURCE)
        @IntDef(UNSUPPORTED, EPUB)
        annotation class BookType
    }

    data class BookInfo(
        val title: String,
        val author: Author,
    )
}

data class Author(
    val name: String,
    val surname: String

)