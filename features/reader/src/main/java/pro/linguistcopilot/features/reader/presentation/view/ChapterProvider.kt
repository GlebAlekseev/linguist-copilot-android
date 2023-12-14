package pro.linguistcopilot.features.reader.presentation.view

import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.net.Uri
import android.os.Build
import android.text.Layout
import android.text.StaticLayout
import android.text.TextPaint
import pro.linguistcopilot.core.utils.dpToPx
import pro.linguistcopilot.core.utils.spToPx
import pro.linguistcopilot.features.reader.core.BookReader
import pro.linguistcopilot.features.reader.core.isContentScheme
import pro.linguistcopilot.features.reader.domain.Book
import pro.linguistcopilot.features.reader.domain.BookChapter
import pro.linguistcopilot.features.reader.presentation.view.entities.TextChapter
import pro.linguistcopilot.features.reader.presentation.view.entities.TextLine
import pro.linguistcopilot.features.reader.presentation.view.entities.TextPage
import pro.linguistcopilot.features.reader.presentation.view.entities.column.ImageColumn
import pro.linguistcopilot.features.reader.presentation.view.entities.column.ReviewColumn
import pro.linguistcopilot.features.reader.presentation.view.entities.column.TextColumn
import pro.linguistcopilot.features.reader.presentation.view.utils.RealPathUtils
import pro.linguistcopilot.features.reader.presentation.view.utils.splitNotBlank
import pro.linguistcopilot.features.reader.presentation.view.utils.textHeight
import pro.linguistcopilot.features.reader.presentation.view.utils.toStringArray
import splitties.init.appCtx
import java.util.LinkedList
import java.util.regex.Pattern

class ChapterProvider(
    private val bookReader: BookReader,
    private val textPageFactoryCallback: () -> TextPage
) {
    companion object {
        private const val srcReplaceChar = "▩"
        private const val reviewChar = "▨"

        private const val TEXT_FONT = ""
        private const val LINE_SPACING_EXTRA = 12
        private const val PARAGRAPH_SPACING = 2
        private const val TITLE_TOP_SPACING = 0
        private const val TITLE_BOTTOM_SPACING = 0
        private const val PARAGRAPH_INDENT = "　　"

        private const val PADDING_LEFT = 16
        private const val PADDING_RIGHT = 16
        private const val PADDING_TOP = 6
        private const val PADDING_BOTTOM = 6

        private const val TEXT_BOLD = 0
        private val TEXT_COLOR = Color.parseColor("#3E3D3B")
        private const val LETTER_SPACING = 0.1f
        private const val TITLE_FONT = 0.1f
        private const val TEXT_SIZE = 20
        private const val TITLE_SIZE = 0
        private const val TEXT_FULL_JUSTIFY = true
        private const val TITLE_MODE = 0
        val isMiddleTitle: Boolean get() = TITLE_MODE == 1

    }


    private val book: Book get() = bookReader.book

    var viewWidth = 0
        private set

    var viewHeight = 0
        private set

    var paddingLeft = 0
        private set

    var paddingTop = 0
        private set

    var paddingRight = 0
        private set

    var paddingBottom = 0
        private set

    var visibleWidth = 0
        private set

    var visibleHeight = 0
        private set

    var visibleRight = 0
        private set

    var visibleBottom = 0
        private set

    var lineSpacingExtra = 0f
        private set

    private var paragraphSpacing = 0

    private var titleTopSpacing = 0

    private var titleBottomSpacing = 0

    private var indentCharWidth = 0f

    private var titlePaintTextHeight = 0f

    var contentPaintTextHeight = 0f

    private var titlePaintFontMetrics = Paint.FontMetrics()

    var contentPaintFontMetrics = Paint.FontMetrics()

    var typeface: Typeface? = Typeface.DEFAULT
        private set

    var titlePaint: TextPaint = TextPaint()

    var contentPaint: TextPaint = TextPaint()

    var reviewPaint: TextPaint = TextPaint()

    var doublePage = false
        private set

    init {
        upStyle()
    }

    suspend fun getTextChapter(
        bookChapter: BookChapter,
        displayTitle: String,
        bookContent: String,
        chapterSize: Int,
    ): TextChapter {
        val content = bookContent
        val textPages = arrayListOf<TextPage>()
        val stringBuilder = StringBuilder()
        var absStartX = paddingLeft
        var durY = 0f
        textPages.add(textPageFactoryCallback())
        if (TITLE_MODE != 2 || bookChapter.isVolume) {
            displayTitle.splitNotBlank("\n").forEach { text ->
                setTypeText(
                    book, absStartX, durY,
                    if (false) text + reviewChar else text,
                    textPages,
                    stringBuilder,
                    titlePaint,
                    titlePaintTextHeight,
                    titlePaintFontMetrics,
                    isTitle = true,
                    emptyContent = content.isEmpty(),
                    isVolumeTitle = bookChapter.isVolume
                ).let {
                    absStartX = it.first
                    durY = it.second
                }
            }
            durY += titleBottomSpacing
        }
        if (true) {
            var text = content.replace(srcReplaceChar, "▣")
            val srcList = LinkedList<String>()
            val sb = StringBuffer()
            val matcher =
                Pattern.compile("<img[^>]*src=\"([^\"]*(?:\"[^>]+\\})?)\"[^>]*>").matcher(text)
            while (matcher.find()) {
                matcher.group(1)?.let { src ->
                    srcList.add(src)
                    matcher.appendReplacement(sb, srcReplaceChar)
                }
            }
            matcher.appendTail(sb)
            text = sb.toString()
            setTypeText(
                book,
                absStartX,
                durY,
                text,
                textPages,
                stringBuilder,
                contentPaint,
                contentPaintTextHeight,
                contentPaintFontMetrics,
                srcList = srcList
            ).let {
                absStartX = it.first
                durY = it.second
            }
        }
        textPages.last().height = durY + 20.dpToPx()
        textPages.last().text = stringBuilder.toString()
        textPages.forEachIndexed { index, item ->
            item.index = index
            item.pageSize = textPages.size
            item.chapterIndex = bookChapter.index
            item.chapterSize = chapterSize
            item.title = displayTitle
            item.upLinesPosition()
        }
        return TextChapter(
            bookChapter,
            bookChapter.index,
            displayTitle,
            textPages,
            chapterSize,
            false,
        )
    }


    private suspend fun setTypeText(
        book: Book,
        x: Int,
        y: Float,
        text: String,
        textPages: ArrayList<TextPage>,
        stringBuilder: StringBuilder,
        textPaint: TextPaint,
        textHeight: Float,
        fontMetrics: Paint.FontMetrics,
        isTitle: Boolean = false,
        emptyContent: Boolean = false,
        isVolumeTitle: Boolean = false,
        srcList: LinkedList<String>? = null
    ): Pair<Int, Float> {
        var absStartX = x
        val widthsArray = FloatArray(text.length)
        val layout = run {
            textPaint.getTextWidths(text, widthsArray)
            StaticLayout(text, textPaint, visibleWidth, Layout.Alignment.ALIGN_NORMAL, 0f, 0f, true)
        }
        val widthsList = widthsArray.asList()
        var durY = when {
            emptyContent && textPages.size == 1 -> {
                val textPage = textPages.last()
                if (textPage.lineSize == 0) {
                    val ty = (visibleHeight - layout.lineCount * textHeight) / 2
                    if (ty > titleTopSpacing) ty else titleTopSpacing.toFloat()
                } else {
                    var textLayoutHeight = layout.lineCount * textHeight
                    val fistLine = textPage.getLine(0)
                    if (fistLine.lineTop < textLayoutHeight + titleTopSpacing) {
                        textLayoutHeight = fistLine.lineTop - titleTopSpacing
                    }
                    textPage.lines.forEach {
                        it.lineTop = it.lineTop - textLayoutHeight
                        it.lineBase = it.lineBase - textLayoutHeight
                        it.lineBottom = it.lineBottom - textLayoutHeight
                    }
                    y - textLayoutHeight
                }
            }

            isTitle && textPages.size == 1 && textPages.last().lines.isEmpty() ->
                y + titleTopSpacing

            else -> y
        }
        for (lineIndex in 0 until layout.lineCount) {
            val textLine = TextLine(this, isTitle = isTitle)
            if (durY + textHeight > visibleHeight) {
                val textPage = textPages.last()
                if (doublePage && absStartX < viewWidth / 2) {
                    textPage.leftLineSize = textPage.lineSize
                    absStartX = viewWidth / 2 + paddingLeft
                } else {
                    if (textPage.leftLineSize == 0) {
                        textPage.leftLineSize = textPage.lineSize
                    }
                    textPage.text = stringBuilder.toString()
                    textPage.height = durY
                    textPages.add(textPageFactoryCallback())
                    stringBuilder.clear()
                    absStartX = paddingLeft
                }
                durY = 0f
            }
            val lineStart = layout.getLineStart(lineIndex)
            val lineEnd = layout.getLineEnd(lineIndex)
            val words = text.substring(lineStart, lineEnd)
            val textWidths = widthsList.subList(lineStart, lineEnd)
            val desiredWidth = textWidths.sum()
            when {
                lineIndex == 0 && layout.lineCount > 1 && !isTitle -> {
                    textLine.text = words
                    addCharsToLineFirst(
                        book, absStartX, textLine, words,
                        textPaint, desiredWidth, textWidths, srcList
                    )
                }

                lineIndex == layout.lineCount - 1 -> {
                    textLine.text = words
                    textLine.isParagraphEnd = true
                    val startX = if (
                        isTitle &&
                        (isMiddleTitle || emptyContent || isVolumeTitle)
                    ) {
                        (visibleWidth - desiredWidth) / 2
                    } else {
                        0f
                    }
                    addCharsToLineNatural(
                        book, absStartX, textLine, words, textPaint,
                        startX, !isTitle && lineIndex == 0, textWidths, srcList
                    )
                }

                else -> {
                    if (
                        isTitle &&
                        (isMiddleTitle || emptyContent || isVolumeTitle)
                    ) {
                        val startX = (visibleWidth - desiredWidth) / 2
                        addCharsToLineNatural(
                            book, absStartX, textLine, words,
                            textPaint, startX, false, textWidths, srcList
                        )
                    } else {
                        textLine.text = words
                        addCharsToLineMiddle(
                            book, absStartX, textLine, words,
                            textPaint, desiredWidth, 0f, textWidths, srcList
                        )
                    }
                }
            }
            val sbLength = stringBuilder.length
            stringBuilder.append(words)
            if (textLine.isParagraphEnd) {
                stringBuilder.append("\n")
            }
            val lastLine = textPages.last().lines.lastOrNull { it.paragraphNum > 0 }
                ?: textPages.getOrNull(textPages.lastIndex - 1)?.lines?.lastOrNull { it.paragraphNum > 0 }
            val paragraphNum = when {
                lastLine == null -> 1
                lastLine.isParagraphEnd -> lastLine.paragraphNum + 1
                else -> lastLine.paragraphNum
            }
            textLine.paragraphNum = paragraphNum
            textLine.chapterPosition =
                (textPages.getOrNull(textPages.lastIndex - 1)?.lines?.lastOrNull()?.run {
                    chapterPosition + charSize + if (isParagraphEnd) 1 else 0
                } ?: 0) + sbLength
            textLine.pagePosition = sbLength
            textPages.last().addLine(textLine)
            textLine.upTopBottom(durY, textHeight, fontMetrics)
            durY += textHeight * lineSpacingExtra
            textPages.last().height = durY
        }
        durY += textHeight * paragraphSpacing / 10f
        return Pair(absStartX, durY)
    }

    private suspend fun addCharsToLineFirst(
        book: Book,
        absStartX: Int,
        textLine: TextLine,
        text: String,
        textPaint: TextPaint,
        desiredWidth: Float,
        textWidths: List<Float>,
        srcList: LinkedList<String>?
    ) {
        var x = 0f
        if (!TEXT_FULL_JUSTIFY) {
            addCharsToLineNatural(
                book, absStartX, textLine, text, textPaint,
                x, true, textWidths, srcList
            )
            return
        }
        val bodyIndent = PARAGRAPH_INDENT
        for (char in bodyIndent.toStringArray()) {
            val x1 = x + indentCharWidth
            textLine.addColumn(
                TextColumn(
                    charData = char,
                    start = absStartX + x,
                    end = absStartX + x1
                )
            )
            x = x1
            textLine.indentWidth = x
        }
        if (text.length > bodyIndent.length) {
            val text1 = text.substring(bodyIndent.length, text.length)
            val textWidths1 = textWidths.subList(bodyIndent.length, textWidths.size)
            addCharsToLineMiddle(
                book, absStartX, textLine, text1,
                textPaint, desiredWidth, x, textWidths1, srcList
            )
        }
    }

    private suspend fun addCharsToLineMiddle(
        book: Book,
        absStartX: Int,
        textLine: TextLine,
        text: String,
        textPaint: TextPaint,
        desiredWidth: Float,
        startX: Float,
        textWidths: List<Float>,
        srcList: LinkedList<String>?
    ) {
        if (!TEXT_FULL_JUSTIFY) {
            addCharsToLineNatural(
                book, absStartX, textLine, text, textPaint,
                startX, false, textWidths, srcList
            )
            return
        }
        val residualWidth = visibleWidth - desiredWidth
        val spaceSize = text.count { it == ' ' }
        val (words, widths) = getStringArrayAndTextWidths(text, textWidths, textPaint)
        if (spaceSize > 1) {
            val d = residualWidth / spaceSize
            var x = startX
            words.forEachIndexed { index, char ->
                val cw = widths[index]
                val x1 = if (char == " ") {
                    if (index != words.lastIndex) (x + cw + d) else (x + cw)
                } else {
                    (x + cw)
                }
                addCharToLine(
                    book, absStartX, textLine, char,
                    x, x1, index + 1 == words.size, srcList
                )
                x = x1
            }
        } else {
            val gapCount: Int = words.lastIndex
            val d = residualWidth / gapCount
            var x = startX
            words.forEachIndexed { index, char ->
                val cw = widths[index]
                val x1 = if (index != words.lastIndex) (x + cw + d) else (x + cw)
                addCharToLine(
                    book, absStartX, textLine, char,
                    x, x1, index + 1 == words.size, srcList
                )
                x = x1
            }
        }
        exceed(absStartX, textLine, words)
    }

    private suspend fun addCharsToLineNatural(
        book: Book,
        absStartX: Int,
        textLine: TextLine,
        text: String,
        textPaint: TextPaint,
        startX: Float,
        hasIndent: Boolean,
        textWidths: List<Float>,
        srcList: LinkedList<String>?
    ) {
        val indentLength = PARAGRAPH_INDENT.length
        var x = startX
        val (words, widths) = getStringArrayAndTextWidths(text, textWidths, textPaint)
        words.forEachIndexed { index, char ->
            val cw = widths[index]
            val x1 = x + cw
            addCharToLine(book, absStartX, textLine, char, x, x1, index + 1 == words.size, srcList)
            x = x1
            if (hasIndent && index == indentLength - 1) {
                textLine.indentWidth = x
            }
        }
        exceed(absStartX, textLine, words)
    }

    fun getStringArrayAndTextWidths(
        text: String,
        textWidths: List<Float>,
        textPaint: TextPaint
    ): Pair<List<String>, List<Float>> {
        val charArray = text.toCharArray()
        val strList = ArrayList<String>()
        val textWidthList = ArrayList<Float>()
        val lastIndex = charArray.lastIndex
        for (i in textWidths.indices) {
            if (charArray[i].isLowSurrogate()) {
                continue
            }
            val char = if (i + 1 <= lastIndex && charArray[i + 1].isLowSurrogate()) {
                charArray[i].toString() + charArray[i + 1].toString()
            } else {
                charArray[i].toString()
            }
            val w = textWidths[i]
            if (w == 0f && textWidthList.size > 0) {
                textWidthList[textWidthList.lastIndex] = textPaint.measureText(strList.last())
                textWidthList.add(textPaint.measureText(char))
            } else {
                textWidthList.add(w)
            }
            strList.add(char)
        }
        return strList to textWidthList
    }

    private suspend fun addCharToLine(
        book: Book,
        absStartX: Int,
        textLine: TextLine,
        char: String,
        xStart: Float,
        xEnd: Float,
        isLineEnd: Boolean,
        srcList: LinkedList<String>?
    ) {
        val column = when {
            srcList != null && char == srcReplaceChar -> {
                val src = srcList.removeFirst()
                ImageColumn(
                    start = absStartX + xStart,
                    end = absStartX + xEnd,
                    src = src
                )
            }

            isLineEnd && char == reviewChar -> {
                ReviewColumn(
                    start = absStartX + xStart,
                    end = absStartX + xEnd,
                    count = 100
                )
            }

            else -> {
                TextColumn(
                    start = absStartX + xStart,
                    end = absStartX + xEnd,
                    charData = char
                )
            }
        }
        textLine.addColumn(column)
    }

    private fun exceed(absStartX: Int, textLine: TextLine, words: List<String>) {
        val visibleEnd = absStartX + visibleWidth
        val endX = textLine.columns.lastOrNull()?.end ?: return
        if (endX > visibleEnd) {
            val cc = (endX - visibleEnd) / words.size
            for (i in 0..words.lastIndex) {
                textLine.getColumnReverseAt(i).let {
                    val py = cc * (words.size - i)
                    it.start = it.start - py
                    it.end = it.end - py
                }
            }
        }
    }

    fun upStyle() {
        typeface = getTypeface(TEXT_FONT)
        getPaints(typeface).let {
            titlePaint = it.first
            contentPaint = it.second
            reviewPaint.color = contentPaint.color
            reviewPaint.textSize = contentPaint.textSize * 0.45f
            reviewPaint.textAlign = Paint.Align.CENTER
        }

        lineSpacingExtra = LINE_SPACING_EXTRA / 10f
        paragraphSpacing = PARAGRAPH_SPACING
        titleTopSpacing = TITLE_TOP_SPACING.dpToPx()
        titleBottomSpacing = TITLE_BOTTOM_SPACING.dpToPx()
        val bodyIndent = PARAGRAPH_INDENT
        indentCharWidth = StaticLayout.getDesiredWidth(bodyIndent, contentPaint) / bodyIndent.length
        titlePaintTextHeight = titlePaint.textHeight
        contentPaintTextHeight = contentPaint.textHeight
        titlePaintFontMetrics = titlePaint.fontMetrics
        contentPaintFontMetrics = contentPaint.fontMetrics
        upLayout()
    }

    private fun getTypeface(fontPath: String): Typeface? {
        return kotlin.runCatching {
            when {
                fontPath.isContentScheme() && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O -> {
                    appCtx.contentResolver
                        .openFileDescriptor(Uri.parse(fontPath), "r")!!
                        .use {
                            Typeface.Builder(it.fileDescriptor).build()
                        }
                }

                fontPath.isContentScheme() -> {
                    Typeface.createFromFile(RealPathUtils.getPath(appCtx, Uri.parse(fontPath)))
                }

                fontPath.isNotEmpty() -> Typeface.createFromFile(fontPath)
                else -> when (1) {
                    1 -> Typeface.SERIF
                    2 -> Typeface.MONOSPACE
                    else -> Typeface.SANS_SERIF
                }
            }
        }.getOrElse {
            Typeface.SANS_SERIF
        } ?: Typeface.DEFAULT
    }

    private fun getPaints(typeface: Typeface?): Pair<TextPaint, TextPaint> {
        val bold = Typeface.create(typeface, Typeface.BOLD)
        val normal = Typeface.create(typeface, Typeface.NORMAL)
        val (titleFont, textFont) = when (TEXT_BOLD) {
            1 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    Pair(Typeface.create(typeface, 900, false), bold)
                else
                    Pair(bold, bold)
            }

            2 -> {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P)
                    Pair(normal, Typeface.create(typeface, 300, false))
                else
                    Pair(normal, normal)
            }

            else -> Pair(bold, normal)
        }

        val tPaint = TextPaint()
        tPaint.color = TEXT_COLOR
        tPaint.letterSpacing = LETTER_SPACING
        tPaint.typeface = titleFont
        tPaint.textSize = (TEXT_SIZE + TITLE_SIZE).toFloat().spToPx()
        tPaint.isAntiAlias = true

        val cPaint = TextPaint()
        cPaint.color = TEXT_COLOR
        cPaint.letterSpacing = LETTER_SPACING
        cPaint.typeface = textFont
        cPaint.textSize = TEXT_SIZE.toFloat().spToPx()
        cPaint.isAntiAlias = true
        return Pair(tPaint, cPaint)
    }

    fun upViewSize(width: Int, height: Int) {
        if (width > 0 && height > 0 && (width != viewWidth || height != viewHeight)) {
            viewWidth = width
            viewHeight = height
            upLayout()
        }
    }

    fun upLayout() {
        doublePage = false

        if (viewWidth > 0 && viewHeight > 0) {
            paddingLeft = PADDING_LEFT.dpToPx()
            paddingTop = PADDING_TOP.dpToPx()
            paddingRight = PADDING_RIGHT.dpToPx()
            paddingBottom = PADDING_BOTTOM.dpToPx()
            visibleWidth = if (doublePage) {
                viewWidth / 2 - paddingLeft - paddingRight
            } else {
                viewWidth - paddingLeft - paddingRight
            }
            visibleHeight = viewHeight - paddingTop - paddingBottom
            visibleRight = viewWidth - paddingRight
            visibleBottom = paddingTop + visibleHeight
        }
    }

}