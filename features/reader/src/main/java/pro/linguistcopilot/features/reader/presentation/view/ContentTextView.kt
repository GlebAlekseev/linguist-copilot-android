package pro.linguistcopilot.features.reader.presentation.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import pro.linguistcopilot.features.reader.presentation.view.entities.TextLine
import pro.linguistcopilot.features.reader.presentation.view.entities.TextPage
import pro.linguistcopilot.features.reader.presentation.view.entities.column.TextColumn
import pro.linguistcopilot.features.reader.presentation.view.utils.accentColor
import pro.linguistcopilot.features.reader.presentation.view.utils.getCompatColor


class ContentTextView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    private var pageOffset = 0
    var readBook: ReadBook? = null
    var pageFactory: PageFactory<TextPage>? = null

    var textPage: TextPage? = null
        private set

    private val imagePaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    private val visibleRect = RectF()
    var isMainView = false

    private val selectedPaint by lazy {
        Paint().apply {
            color = context.getCompatColor(pro.linguistcopilot.design.R.color.black)
            style = Paint.Style.FILL
        }
    }

    fun resetPageOffset() {
        pageOffset = 0
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (textPage != null && pageFactory != null && readBook?.chapterProvider != null) {
            drawPage(canvas)
        }
    }

    private fun drawPage(canvas: Canvas) {
        var relativeOffset = relativeOffset(0)
        textPage!!.lines.forEach { textLine ->
            drawLine(canvas, textPage!!, textLine, relativeOffset)
        }
    }

    private fun relativeOffset(relativePos: Int): Float {
        return when (relativePos) {
            0 -> pageOffset.toFloat()
            1 -> pageOffset + textPage!!.height
            else -> pageOffset + textPage!!.height + pageFactory!!.nextPage.height
        }
    }

    fun relativePage(relativePos: Int): TextPage {
        return when (relativePos) {
            0 -> textPage!!
            1 -> pageFactory!!.nextPage
            else -> pageFactory!!.nextPlusPage
        }
    }

    private fun drawLine(
        canvas: Canvas,
        textPage: TextPage,
        textLine: TextLine,
        relativeOffset: Float,
    ) {
        val lineTop = textLine.lineTop + relativeOffset
        val lineBase = textLine.lineBase + relativeOffset
        val lineBottom = textLine.lineBottom + relativeOffset
        drawChars(canvas, textPage, textLine, lineTop, lineBase, lineBottom)
    }

    private fun drawChars(
        canvas: Canvas,
        textPage: TextPage,
        textLine: TextLine,
        lineTop: Float,
        lineBase: Float,
        lineBottom: Float,
    ) {

        val textPaint = if (textLine.isTitle) {
            readBook!!.chapterProvider.titlePaint
        } else {
            readBook!!.chapterProvider.contentPaint
        }
        val textColor =
            if (textLine.isReadAloud) context.accentColor else Color.parseColor("#FEFD3B")
        (textLine.columns).forEach {
            when (it) {
                is TextColumn -> {
                    textPaint.color = textColor
                    if (it.isSearchResult) {
                        textPaint.color = context.accentColor
                    }
                    canvas.drawText(
                        it.charData,
                        it.start,
                        lineBase,
                        textPaint.apply {
                            color = Color.BLACK
                        })
                    if (it.selected) {
                        canvas.drawRect(it.start, lineTop, it.end, lineBottom, selectedPaint)
                    }
                }
            }
        }
    }


    fun setContent(textPage: TextPage) {
        this.textPage = textPage
        invalidate()
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!isMainView) return
        readBook?.chapterProvider?.upViewSize(w, h)
        upVisibleRect()
        textPage?.format()
    }

    fun upVisibleRect() {
        readBook?.chapterProvider?.let { chapterProvider ->
            visibleRect.set(
                chapterProvider.paddingLeft.toFloat(),
                chapterProvider.paddingTop.toFloat(),
                chapterProvider.visibleRight.toFloat(),
                chapterProvider.visibleBottom.toFloat()
            )
        }
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return pageFactory?.hasNext() ?: false
    }
}
