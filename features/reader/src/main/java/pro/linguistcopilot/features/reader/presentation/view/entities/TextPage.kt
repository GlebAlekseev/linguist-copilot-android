package pro.linguistcopilot.features.reader.presentation.view.entities

import android.text.Layout
import android.text.StaticLayout
import pro.linguistcopilot.features.reader.presentation.view.ReadBook
import pro.linguistcopilot.features.reader.presentation.view.entities.column.TextColumn
import java.text.DecimalFormat
import kotlin.math.min

data class TextPage(
    val readBook: ReadBook,
    var index: Int = 0,
    var text: String = "appCtx.getString(R.string.data_loading)",
    var title: String = "appCtx.getString(R.string.data_loading)",
    private val textLines: ArrayList<TextLine> = arrayListOf(),
    var pageSize: Int = 0,
    var chapterSize: Int = 0,
    var chapterIndex: Int = 0,
    var height: Float = 0f,
    var leftLineSize: Int = 0,
) {
    private val chapterProvider get() = readBook.chapterProvider
    val lines: List<TextLine> get() = textLines
    val lineSize: Int get() = textLines.size
    val charSize: Int get() = text.length.coerceAtLeast(1)
    val searchResult = hashSetOf<TextColumn>()
    var isMsgPage: Boolean = false

    val paragraphs by lazy {
        paragraphsInternal
    }

    val paragraphsInternal: ArrayList<TextParagraph>
        get() {
            val paragraphs = arrayListOf<TextParagraph>()
            val lines = textLines.filter { it.paragraphNum > 0 }
            val offset = lines.first().paragraphNum - 1
            lines.forEach { line ->
                if (paragraphs.lastIndex < line.paragraphNum - offset - 1) {
                    paragraphs.add(TextParagraph(0))
                }
                paragraphs[line.paragraphNum - offset - 1].textLines.add(line)
            }
            return paragraphs
        }

    fun addLine(line: TextLine) {
        textLines.add(line)
    }

    fun getLine(index: Int): TextLine {
        return textLines.getOrElse(index) {
            textLines.last()
        }
    }

    fun upLinesPosition() {
        if (!true) return
        if (textLines.size <= 1) return
        if (leftLineSize == 0) {
            leftLineSize = lineSize
        }
        chapterProvider.run {
            val lastLine = textLines[leftLineSize - 1]
            if (lastLine.isImage) return@run
            val lastLineHeight = with(lastLine) { lineBottom - lineTop }
            val pageHeight = lastLine.lineBottom + contentPaintTextHeight * lineSpacingExtra
            if (visibleHeight - pageHeight >= lastLineHeight) return@run
            val surplus = (visibleBottom - lastLine.lineBottom)
            if (surplus == 0f) return@run
            height += surplus
            val tj = surplus / (leftLineSize - 1)
            for (i in 1 until leftLineSize) {
                val line = textLines[i]
                line.lineTop = line.lineTop + tj * i
                line.lineBase = line.lineBase + tj * i
                line.lineBottom = line.lineBottom + tj * i
            }
        }
        if (leftLineSize == lineSize) return
        chapterProvider.run {
            val lastLine = textLines.last()
            if (lastLine.isImage) return@run
            val lastLineHeight = with(lastLine) { lineBottom - lineTop }
            val pageHeight = lastLine.lineBottom + contentPaintTextHeight * lineSpacingExtra
            if (visibleHeight - pageHeight >= lastLineHeight) return@run
            val surplus = (visibleBottom - lastLine.lineBottom)
            if (surplus == 0f) return@run
            val tj = surplus / (textLines.size - leftLineSize - 1)
            for (i in leftLineSize + 1 until textLines.size) {
                val line = textLines[i]
                val surplusIndex = i - leftLineSize
                line.lineTop = line.lineTop + tj * surplusIndex
                line.lineBase = line.lineBase + tj * surplusIndex
                line.lineBottom = line.lineBottom + tj * surplusIndex
            }
        }
    }

    @Suppress("DEPRECATION")
    fun format(): TextPage {
        if (textLines.isEmpty()) isMsgPage = true
        if (isMsgPage && chapterProvider.viewWidth > 0) {
            textLines.clear()
            val visibleWidth = chapterProvider.visibleRight - chapterProvider.paddingLeft
            val layout = StaticLayout(
                text, chapterProvider.contentPaint, visibleWidth,
                Layout.Alignment.ALIGN_NORMAL, 1f, 0f, false
            )
            var y = (chapterProvider.visibleHeight - layout.height) / 2f
            if (y < 0) y = 0f
            for (lineIndex in 0 until layout.lineCount) {
                val textLine = TextLine(
                    chapterProvider = chapterProvider
                )
                textLine.lineTop = chapterProvider.paddingTop + y + layout.getLineTop(lineIndex)
                textLine.lineBase =
                    chapterProvider.paddingTop + y + layout.getLineBaseline(lineIndex)
                textLine.lineBottom =
                    chapterProvider.paddingTop + y + layout.getLineBottom(lineIndex)
                var x = chapterProvider.paddingLeft +
                        (visibleWidth - layout.getLineMax(lineIndex)) / 2
                textLine.text =
                    text.substring(layout.getLineStart(lineIndex), layout.getLineEnd(lineIndex))
                for (i in textLine.text.indices) {
                    val char = textLine.text[i].toString()
                    val cw = StaticLayout.getDesiredWidth(char, chapterProvider.contentPaint)
                    val x1 = x + cw
                    textLine.addColumn(
                        TextColumn(start = x, end = x1, char)
                    )
                    x = x1
                }
                textLines.add(textLine)
            }
            height = chapterProvider.visibleHeight.toFloat()
        }
        return this
    }

    fun removePageAloudSpan(): TextPage {
        textLines.forEach { textLine ->
            textLine.isReadAloud = false
        }
        return this
    }

    fun upPageAloudSpan(aloudSpanStart: Int) {
        removePageAloudSpan()
        var lineStart = 0
        for ((index, textLine) in textLines.withIndex()) {
            val lineLength = textLine.text.length + if (textLine.isParagraphEnd) 1 else 0
            if (aloudSpanStart > lineStart && aloudSpanStart < lineStart + lineLength) {
                for (i in index - 1 downTo 0) {
                    if (textLines[i].isParagraphEnd) {
                        break
                    } else {
                        textLines[i].isReadAloud = true
                    }
                }
                for (i in index until textLines.size) {
                    if (textLines[i].isParagraphEnd) {
                        textLines[i].isReadAloud = true
                        break
                    } else {
                        textLines[i].isReadAloud = true
                    }
                }
                break
            }
            lineStart += lineLength
        }
    }

    val readProgress: String
        get() {
            val df = DecimalFormat("0.0%")
            if (chapterSize == 0 || pageSize == 0 && chapterIndex == 0) {
                return "0.0%"
            } else if (pageSize == 0) {
                return df.format((chapterIndex + 1.0f) / chapterSize.toDouble())
            }
            var percent =
                df.format(chapterIndex * 1.0f / chapterSize + 1.0f / chapterSize * (index + 1) / pageSize.toDouble())
            if (percent == "100.0%" && (chapterIndex + 1 != chapterSize || index + 1 != pageSize)) {
                percent = "99.9%"
            }
            return percent
        }

    fun getPosByLineColumn(lineIndex: Int, columnIndex: Int): Int {
        var length = 0
        val maxIndex = min(lineIndex, lineSize)
        for (index in 0 until maxIndex) {
            length += textLines[index].charSize
            if (textLines[index].isParagraphEnd) {
                length++
            }
        }
        return length + columnIndex
    }

    fun getTextChapter(): TextChapter? {
        readBook.currentChapter?.let {
            if (it.position == chapterIndex) {
                return it
            }
        }
        readBook.nextChapter?.let {
            if (it.position == chapterIndex) {
                return it
            }
        }
        readBook.prevChapter?.let {
            if (it.position == chapterIndex) {
                return it
            }
        }
        return null
    }

    fun hasImageOrEmpty(): Boolean {
        return textLines.any { it.isImage } || textLines.isEmpty()
    }
}
