package pro.linguistcopilot.features.reader.presentation.view3.api

import pro.linguistcopilot.features.reader.presentation.view3.entities.TextChapter
import pro.linguistcopilot.features.reader.presentation.view3.ReadBook

interface DataSource {

    val pageIndex: Int get() = ReadBook.durPageIndex

    val currentChapter: TextChapter?

    val nextChapter: TextChapter?

    val prevChapter: TextChapter?

    fun hasNextChapter(): Boolean

    fun hasPrevChapter(): Boolean

    fun upContent(relativePosition: Int = 0, resetPageOffset: Boolean = true)
}