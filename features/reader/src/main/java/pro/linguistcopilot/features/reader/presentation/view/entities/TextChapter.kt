package pro.linguistcopilot.features.reader.presentation.view.entities


import pro.linguistcopilot.features.reader.domain.BookChapter
import kotlin.math.min

data class TextChapter(
    val chapter: BookChapter,
    val position: Int,
    val title: String,
    val pages: List<TextPage>,
    val chaptersSize: Int,
    val sameTitleRemoved: Boolean,
) {
    fun getPage(index: Int): TextPage? {
        return pages.getOrNull(index)
    }

    fun getPageByReadPos(readPos: Int): TextPage? {
        return getPage(getPageIndexByCharIndex(readPos))
    }

    val lastPage: TextPage? get() = pages.lastOrNull()

    val lastIndex: Int get() = pages.lastIndex

    val lastReadLength: Int get() = getReadLength(lastIndex)

    val pageSize: Int get() = pages.size

    val paragraphs by lazy {
        paragraphsInternal
    }

    val pageParagraphs by lazy {
        pageParagraphsInternal
    }

    val paragraphsInternal: ArrayList<TextParagraph>
        get() {
            val paragraphs = arrayListOf<TextParagraph>()
            pages.forEach {
                it.lines.forEach loop@{ line ->
                    if (line.paragraphNum <= 0) return@loop
                    if (paragraphs.lastIndex < line.paragraphNum - 1) {
                        paragraphs.add(TextParagraph(line.paragraphNum))
                    }
                    paragraphs[line.paragraphNum - 1].textLines.add(line)
                }
            }
            return paragraphs
        }

    val pageParagraphsInternal: List<TextParagraph>
        get() = pages.map {
            it.paragraphs
        }.flatten().also {
            it.forEachIndexed { index, textParagraph ->
                textParagraph.num = index + 1
            }
        }

    fun isLastIndex(index: Int): Boolean {
        return index >= pages.size - 1
    }

    fun getReadLength(pageIndex: Int): Int {
        var length = 0
        val maxIndex = min(pageIndex, pages.size)
        for (index in 0 until maxIndex) {
            length += pages[index].charSize
        }
        return length
    }

    fun getNextPageLength(length: Int): Int {
        val pageIndex = getPageIndexByCharIndex(length)
        if (pageIndex + 1 >= pageSize) {
            return -1
        }
        return getReadLength(pageIndex + 1)
    }

    fun getPrevPageLength(length: Int): Int {
        val pageIndex = getPageIndexByCharIndex(length)
        if (pageIndex - 1 < 0) {
            return -1
        }
        return getReadLength(pageIndex - 1)
    }

    fun getContent(): String {
        val stringBuilder = StringBuilder()
        pages.forEach {
            stringBuilder.append(it.text)
        }
        return stringBuilder.toString()
    }

    fun getUnRead(pageIndex: Int): String {
        val stringBuilder = StringBuilder()
        if (pages.isNotEmpty()) {
            for (index in pageIndex..pages.lastIndex) {
                stringBuilder.append(pages[index].text)
            }
        }
        return stringBuilder.toString()
    }

    fun getNeedReadAloud(pageIndex: Int, pageSplit: Boolean, startPos: Int): String {
        val stringBuilder = StringBuilder()
        if (pages.isNotEmpty()) {
            for (index in pageIndex..pages.lastIndex) {
                stringBuilder.append(pages[index].text)
                if (pageSplit && !stringBuilder.endsWith("\n")) {
                    stringBuilder.append("\n")
                }
            }
        }
        return stringBuilder.substring(startPos).toString()
    }

    fun getParagraphNum(
        position: Int,
        pageSplit: Boolean,
    ): Int {
        val paragraphs = if (pageSplit) {
            pageParagraphs
        } else {
            paragraphs
        }
        paragraphs.forEach { paragraph ->
            if (position in paragraph.chapterIndices) {
                return paragraph.num
            }
        }
        return -1
    }

    fun getLastParagraphPosition(): Int {
        return pageParagraphs.last().chapterPosition
    }

    fun getPageIndexByCharIndex(charIndex: Int): Int {
        var length = 0
        pages.forEach {
            length += it.charSize
            if (length > charIndex) {
                return it.index
            }
        }
        return pages.lastIndex
    }

    fun clearSearchResult() {
        pages.forEach { page ->
            page.searchResult.forEach {
                it.selected = false
                it.isSearchResult = false
            }
            page.searchResult.clear()
        }
    }
}
