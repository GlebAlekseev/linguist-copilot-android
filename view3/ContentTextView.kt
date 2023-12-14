package pro.linguistcopilot.features.reader.presentation.view3

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.os.Build
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat
import pro.linguistcopilot.core.utils.activity
import pro.linguistcopilot.core.utils.dpToPx
import pro.linguistcopilot.features.reader.domain.Book
import pro.linguistcopilot.features.reader.presentation.view3.entities.TextLine
import pro.linguistcopilot.features.reader.presentation.view3.entities.TextPage
import pro.linguistcopilot.features.reader.presentation.view3.entities.TextPos
import pro.linguistcopilot.features.reader.presentation.view3.entities.column.BaseColumn
import pro.linguistcopilot.features.reader.presentation.view3.entities.column.ButtonColumn
import pro.linguistcopilot.features.reader.presentation.view3.entities.column.ImageColumn
import pro.linguistcopilot.features.reader.presentation.view3.entities.column.ReviewColumn
import pro.linguistcopilot.features.reader.presentation.view3.entities.column.TextColumn
import pro.linguistcopilot.features.reader.presentation.view3.provider.ChapterProvider
import pro.linguistcopilot.features.reader.presentation.view3.provider.TextPageFactory
import kotlin.math.min


fun Context.getCompatColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

val Context.accentColor: Int
    get() = Color.MAGENTA

/**
 * 阅读内容视图
 */
class ContentTextView(context: Context, attrs: AttributeSet?) : View(context, attrs) {
    var selectAble = true
    var upView: ((TextPage) -> Unit)? = null
    private val selectedPaint by lazy {
        Paint().apply {
            color = context.getCompatColor(pro.linguistcopilot.design.R.color.black)
            style = Paint.Style.FILL
        }
    }
    private var callBack: CallBack
    private val visibleRect = RectF()
    val selectStart = TextPos(0, 0, 0)
    private val selectEnd = TextPos(0, 0, 0)
    var textPage: TextPage = TextPage()
        private set
    var isMainView = false
    private var drawVisibleImageOnly = false
    private var cacheIncreased = false
    private var longScreenshot = false
    private val increaseSize = 8 * 1024 * 1024
    private val maxCacheSize = if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.N_MR1) {
        min(128 * 1024 * 1024, Runtime.getRuntime().maxMemory())
    } else {
        256 * 1024 * 1024
    }
    var reverseStartCursor = false
    var reverseEndCursor = false

    //滚动参数
    private val pageFactory: TextPageFactory get() = callBack.pageFactory
    private var pageOffset = 0

    //绘制图片的paint
    private val imagePaint by lazy {
        Paint().apply {
            isAntiAlias = true
        }
    }

    init {
        callBack = activity as CallBack
    }

    /**
     * 设置内容
     */
    fun setContent(textPage: TextPage) {
        this.textPage = textPage
        imagePaint.isAntiAlias = true
        invalidate()
    }

    /**
     * 更新绘制区域
     */
    fun upVisibleRect() {
        visibleRect.set(
            ChapterProvider.paddingLeft.toFloat(),
            ChapterProvider.paddingTop.toFloat(),
            ChapterProvider.visibleRight.toFloat(),
            ChapterProvider.visibleBottom.toFloat()
        )
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        if (!isMainView) return
        ChapterProvider.upViewSize(w, h)
        upVisibleRect()
        textPage.format()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (longScreenshot) {
            canvas.translate(0f, scrollY.toFloat())
        }
        canvas.clipRect(visibleRect)
        drawPage(canvas)
        drawVisibleImageOnly = false
        cacheIncreased = false
    }

    /**
     * 绘制页面
     */
    private fun drawPage(canvas: Canvas) {
        var relativeOffset = relativeOffset(0)
        textPage.lines.forEach { textLine ->
            drawLine(canvas, textPage, textLine, relativeOffset)
        }
        if (!callBack.isScroll) return
        //滚动翻页
        if (!pageFactory.hasNext()) return
        val textPage1 = relativePage(1)
        relativeOffset = relativeOffset(1)
        textPage1.lines.forEach { textLine ->
            drawLine(canvas, textPage1, textLine, relativeOffset)
        }
        if (!pageFactory.hasNextPlus()) return
        relativeOffset = relativeOffset(2)
        if (relativeOffset < ChapterProvider.visibleHeight) {
            val textPage2 = relativePage(2)
            textPage2.lines.forEach { textLine ->
                drawLine(canvas, textPage2, textLine, relativeOffset)
            }
        }
    }

    /**
     * 绘制页面
     */
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
//        if (false && ReadBook.book?.isImage != true) {
//            drawUnderline(canvas, textLine, relativeOffset)
//        }
    }

    /**
     * 绘制下划线
     */
    private fun drawUnderline(canvas: Canvas, textLine: TextLine, relativeOffset: Float) {
        val lineY = relativeOffset + textLine.lineBottom - 1.dpToPx()
        canvas.drawLine(
            textLine.lineStart + textLine.indentWidth,
            lineY,
            textLine.lineEnd,
            lineY,
            ChapterProvider.contentPaint
        )
    }

    /**
     * 绘制文字
     */
    private fun drawChars(
        canvas: Canvas,
        textPage: TextPage,
        textLine: TextLine,
        lineTop: Float,
        lineBase: Float,
        lineBottom: Float,
    ) {
        val textPaint = if (textLine.isTitle) {
            ChapterProvider.titlePaint
        } else {
            ChapterProvider.contentPaint
        }
        val textColor =
            if (textLine.isReadAloud) context.accentColor else Color.parseColor("#3E3D3B")
        textLine.columns.forEach {
            when (it) {
                is TextColumn -> {
                    textPaint.color = textColor
                    if (it.isSearchResult) {
                        textPaint.color = context.accentColor
                    }
                    canvas.drawText(it.charData, it.start, lineBase, textPaint)
                    if (it.selected) {
                        canvas.drawRect(it.start, lineTop, it.end, lineBottom, selectedPaint)
                    }
                }

                is ImageColumn -> drawImage(canvas, textPage, textLine, it, lineTop, lineBottom)
                is ReviewColumn -> it.drawToCanvas(canvas, lineBase, textPaint.textSize)
            }
        }
    }

    /**
     * 绘制图片
     */
    @Suppress("UNUSED_PARAMETER")
    private fun drawImage(
        canvas: Canvas,
        textPage: TextPage,
        textLine: TextLine,
        column: ImageColumn,
        lineTop: Float,
        lineBottom: Float
    ) {

        val book = ReadBook.book ?: return
        val isVisible = when {
            lineTop > 0 -> lineTop < height
            lineTop < 0 -> lineBottom > 0
            else -> true
        }
        if (drawVisibleImageOnly && !isVisible) {
            return
        }
//        if (drawVisibleImageOnly &&
//            !cacheIncreased &&
//            ImageProvider.isTriggerRecycled() &&
//            !ImageProvider.isImageAlive(book, column.src)
//        ) {
//            val newSize = ImageProvider.bitmapLruCache.maxSize() + increaseSize
//            if (newSize < maxCacheSize) {
//                ImageProvider.bitmapLruCache.resize(newSize)
//                cacheIncreased = true
//            }
//            return
//        }
        val bitmap = getImageCall?.invoke(
            book,
            column.src,
            (column.end - column.start).toInt(),
            (lineBottom - lineTop).toInt()
        ) {
            if (!drawVisibleImageOnly && isVisible) {
                drawVisibleImageOnly = true
                invalidate()
            }
        } ?: return

        val rectF = if (textLine.isImage) {
            RectF(column.start, lineTop, column.end, lineBottom)
        } else {
            /*以宽度为基准保持图片的原始比例叠加，当div为负数时，允许高度比字符更高*/
            val h = (column.end - column.start) / bitmap.width * bitmap.height
            val div = (lineBottom - lineTop - h) / 2
            RectF(column.start, lineTop + div, column.end, lineBottom - div)
        }
        kotlin.runCatching {
            canvas.drawBitmap(bitmap, null, rectF, imagePaint)
        }.onFailure { e ->
//            context.toastOnUi(e.localizedMessage)
        }
    }

    var getImageCall: (
        (
        book: Book,
        src: String,
        width: Int,
        height: Int?,
        block: (() -> Unit)?
    ) -> Bitmap?
    )? = null

    /**
     * 滚动事件
     */
    fun scroll(mOffset: Int) {
        if (mOffset == 0) return
        pageOffset += mOffset
        if (longScreenshot) {
            scrollY += -mOffset
        }
        if (!pageFactory.hasPrev() && pageOffset > 0) {
            pageOffset = 0
        } else if (!pageFactory.hasNext()
            && pageOffset < 0
            && pageOffset + textPage.height < ChapterProvider.visibleHeight
        ) {
            val offset = (ChapterProvider.visibleHeight - textPage.height).toInt()
            pageOffset = min(0, offset)
        } else if (pageOffset > 0) {
            pageFactory.moveToPrev(true)
            textPage = pageFactory.curPage
            pageOffset -= textPage.height.toInt()
            upView?.invoke(textPage)
            contentDescription = textPage.text
        } else if (pageOffset < -textPage.height) {
            pageOffset += textPage.height.toInt()
            pageFactory.moveToNext(true)
            textPage = pageFactory.curPage
            upView?.invoke(textPage)
            contentDescription = textPage.text
        }
        invalidate()
    }

    /**
     * 重置滚动位置
     */
    fun resetPageOffset() {
        pageOffset = 0
    }

    /**
     * 长按
     */
    fun longPress(
        x: Float,
        y: Float,
        select: (textPos: TextPos) -> Unit,
    ) {
        touch(x, y) { _, textPos, _, _, column ->
            when (column) {
                is ImageColumn -> callBack.onImageLongPress(x, y, column.src)
                is TextColumn -> {
                    if (!selectAble) return@touch
                    column.selected = true
                    invalidate()
                    select(textPos)
                }
            }
        }
    }

    /**
     * 单击
     * @return true:已处理, false:未处理
     */
    @Suppress("UNUSED_ANONYMOUS_PARAMETER")
    fun click(x: Float, y: Float): Boolean {
        var handled = false
        touch(x, y) { _, textPos, textPage, textLine, column ->
            when (column) {
                is ButtonColumn -> {
//                    context.toastOnUi("Button Pressed!")
                    handled = true
                }

                is ReviewColumn -> {
//                    context.toastOnUi("Button Pressed!")
                    handled = true
                }

                is ImageColumn -> if (false) {
//                    activity?.showDialogFragment(PhotoDialog(column.src))
                    handled = true
                }
            }
        }
        return handled
    }

    /**
     * 选择文字
     */
    fun selectText(
        x: Float,
        y: Float,
        select: (textPos: TextPos) -> Unit,
    ) {
        touchRough(x, y) { _, textPos, _, _, column ->
            if (column is TextColumn) {
                column.selected = true
                invalidate()
                select(textPos)
            }
        }
    }

    /**
     * 开始选择符移动
     */
    fun selectStartMove(x: Float, y: Float) {
        touchRough(x, y) { relativeOffset, textPos, _, textLine, textColumn ->
            if (selectStart.compare(textPos) == 0) {
                return@touchRough
            }
            if (textPos.compare(selectEnd) <= 0) {
                selectStart.upData(pos = textPos)
                upSelectedStart(
                    if (textPos.isTouch) textColumn.start else textColumn.end,
                    textLine.lineBottom + relativeOffset,
                    textLine.lineTop + relativeOffset
                )
            } else {
                reverseStartCursor = true
                reverseEndCursor = false
                selectStartMoveIndex(selectEnd)
                selectEnd.upData(textPos)
                upSelectedEnd(
                    if (selectEnd.isTouch || selectEnd.isLast) textColumn.end else textColumn.start,
                    textLine.lineBottom + relativeOffset
                )
            }
            upSelectChars()
        }
    }

    /**
     * 结束选择符移动
     */
    fun selectEndMove(x: Float, y: Float) {
        touchRough(x, y) { relativeOffset, textPos, _, textLine, textColumn ->
            if (textPos.compare(selectEnd) == 0) {
                return@touchRough
            }
            if (textPos.compare(selectStart) >= 0) {
                selectEnd.upData(textPos)
                upSelectedEnd(
                    if (selectEnd.isTouch || selectEnd.isLast) textColumn.end else textColumn.start,
                    textLine.lineBottom + relativeOffset
                )
            } else {
                reverseEndCursor = true
                reverseStartCursor = false
                selectEndMoveIndex(selectStart)
                selectStart.upData(textPos)
                upSelectedStart(
                    if (textPos.isTouch) textColumn.start else textColumn.end,
                    textLine.lineBottom + relativeOffset,
                    textLine.lineTop + relativeOffset
                )
            }
            upSelectChars()
        }
    }

    /**
     * 触碰位置信息
     * @param touched 回调
     */
    private fun touch(
        x: Float,
        y: Float,
        touched: (
            relativeOffset: Float,
            textPos: TextPos,
            textPage: TextPage,
            textLine: TextLine,
            column: BaseColumn
        ) -> Unit
    ) {
        if (!visibleRect.contains(x, y)) return
        var relativeOffset: Float
        for (relativePos in 0..2) {
            relativeOffset = relativeOffset(relativePos)
            if (relativePos > 0) {
                //滚动翻页
                if (!callBack.isScroll) return
                if (relativeOffset >= ChapterProvider.visibleHeight) return
            }
            val textPage = relativePage(relativePos)
            for ((lineIndex, textLine) in textPage.lines.withIndex()) {
                if (textLine.isTouch(x, y, relativeOffset)) {
                    for ((charIndex, textColumn) in textLine.columns.withIndex()) {
                        if (textColumn.isTouch(x)) {
                            touched.invoke(
                                relativeOffset,
                                TextPos(relativePos, lineIndex, charIndex),
                                textPage, textLine, textColumn
                            )
                            return
                        }
                    }
                    return
                }
            }
        }
    }

    /**
     * 触碰位置信息
     * 文本选择专用
     * @param touched 回调
     */
    private fun touchRough(
        x: Float,
        y: Float,
        touched: (
            relativeOffset: Float,
            textPos: TextPos,
            textPage: TextPage,
            textLine: TextLine,
            column: BaseColumn
        ) -> Unit
    ) {
        var relativeOffset: Float
        for (relativePos in 0..2) {
            relativeOffset = relativeOffset(relativePos)
            if (relativePos > 0) {
                //滚动翻页
                if (!callBack.isScroll) return
                if (relativeOffset >= ChapterProvider.visibleHeight) return
            }
            val textPage = relativePage(relativePos)
            for ((lineIndex, textLine) in textPage.lines.withIndex()) {
                if (textLine.isTouchY(y, relativeOffset)) {
                    for ((charIndex, textColumn) in textLine.columns.withIndex()) {
                        if (textColumn.isTouch(x)) {
                            touched.invoke(
                                relativeOffset,
                                TextPos(relativePos, lineIndex, charIndex),
                                textPage, textLine, textColumn
                            )
                            return
                        }
                    }
                    val isLast = textLine.columns.first().start < x
                    val (charIndex, textColumn) = if (isLast) {
                        textLine.columns.withIndex().last()
                    } else {
                        textLine.columns.withIndex().first()
                    }
                    touched.invoke(
                        relativeOffset,
                        TextPos(relativePos, lineIndex, charIndex, false, isLast),
                        textPage, textLine, textColumn
                    )
                    return
                }
            }
        }
    }

    fun getCurVisiblePage(): TextPage {
        val visiblePage = TextPage()
        var relativeOffset: Float
        for (relativePos in 0..2) {
            relativeOffset = relativeOffset(relativePos)
            if (relativePos > 0) {
                //滚动翻页
                if (!callBack.isScroll) break
                if (relativeOffset >= ChapterProvider.visibleHeight) break
            }
            val textPage = relativePage(relativePos)
            for (textLine in textPage.lines) {
                if (textLine.isVisible(relativeOffset)) {
                    val visibleLine = textLine.copy().apply {
                        lineTop += relativeOffset
                        lineBottom += relativeOffset
                    }
                    visiblePage.addLine(visibleLine)
                }
            }
        }
        return visiblePage
    }

    fun getCurVisibleFirstLine(): TextLine? {
        var relativeOffset: Float
        for (relativePos in 0..2) {
            relativeOffset = relativeOffset(relativePos)
            if (relativePos > 0) {
                //滚动翻页
                if (!callBack.isScroll) break
                if (relativeOffset >= ChapterProvider.visibleHeight) break
            }
            val textPage = relativePage(relativePos)
            for (textLine in textPage.lines) {
                if (textLine.isVisible(relativeOffset)) {
                    val visibleLine = textLine.copy().apply {
                        lineTop += relativeOffset
                        lineBottom += relativeOffset
                    }
                    return visibleLine
                }
            }
        }
        return null
    }

    /**
     * 选择开始文字
     */
    fun selectStartMoveIndex(
        relativePagePos: Int,
        lineIndex: Int,
        charIndex: Int,
        isTouch: Boolean,
        isLast: Boolean = false
    ) {
        selectStart.relativePagePos = relativePagePos
        selectStart.lineIndex = lineIndex
        selectStart.columnIndex = charIndex
        selectStart.isTouch = isTouch
        selectStart.isLast = isLast
        val textLine = relativePage(relativePagePos).getLine(lineIndex)
        val textColumn = textLine.getColumn(charIndex)
        upSelectedStart(
            textColumn.start,
            textLine.lineBottom + relativeOffset(relativePagePos),
            textLine.lineTop + relativeOffset(relativePagePos)
        )
        upSelectChars()
    }

    fun selectStartMoveIndex(textPos: TextPos) = textPos.run {
        selectStartMoveIndex(relativePagePos, lineIndex, columnIndex, isTouch, isLast)
    }

    /**
     * 选择结束文字
     */
    fun selectEndMoveIndex(
        relativePage: Int,
        lineIndex: Int,
        charIndex: Int,
        isTouch: Boolean,
        isLast: Boolean = false
    ) {
        selectEnd.relativePagePos = relativePage
        selectEnd.lineIndex = lineIndex
        selectEnd.columnIndex = charIndex
        selectEnd.isTouch = isTouch
        selectEnd.isLast = isLast
        val textLine = relativePage(relativePage).getLine(lineIndex)
        val textColumn = textLine.getColumn(charIndex)
        upSelectedEnd(textColumn.end, textLine.lineBottom + relativeOffset(relativePage))
        upSelectChars()
    }

    fun selectEndMoveIndex(textPos: TextPos) = textPos.run {
        selectEndMoveIndex(relativePagePos, lineIndex, columnIndex, isTouch, isLast)
    }

    private fun upSelectChars() {
        val last = if (callBack.isScroll) 2 else 0
        val textPos = TextPos(0, 0, 0)
        for (relativePos in 0..last) {
            textPos.relativePagePos = relativePos
            val textPage = relativePage(relativePos)
            for ((lineIndex, textLine) in textPage.lines.withIndex()) {
                textPos.lineIndex = lineIndex
                for ((charIndex, column) in textLine.columns.withIndex()) {
                    textPos.columnIndex = charIndex
                    if (column is TextColumn) {
                        val compareStart = textPos.compare(selectStart)
                        val compareEnd = textPos.compare(selectEnd)
                        column.selected = when {
                            compareStart == 0 -> selectStart.isTouch
                            compareEnd == 0 -> selectEnd.isTouch || selectEnd.isLast
                            compareStart > 0 && compareEnd < 0 -> true
                            else -> false
                        }
                        column.isSearchResult =
                            column.selected && callBack.isSelectingSearchResult
                        if (column.isSearchResult) {
                            textPage.searchResult.add(column)
                        }
                    }
                }
            }
        }
        invalidate()
    }

    private fun upSelectedStart(x: Float, y: Float, top: Float) {
        callBack.run {
            upSelectedStart(x, y + headerHeight, top + headerHeight)
        }
    }

    private fun upSelectedEnd(x: Float, y: Float) {
        callBack.run {
            upSelectedEnd(x, y + headerHeight)
        }
    }

    fun resetReverseCursor() {
        reverseStartCursor = false
        reverseEndCursor = false
    }

    fun cancelSelect(clearSearchResult: Boolean = false) {
        val last = if (callBack.isScroll) 2 else 0
        for (relativePos in 0..last) {
            val textPage = relativePage(relativePos)
            textPage.lines.forEach { textLine ->
                textLine.columns.forEach {
                    if (it is TextColumn) {
                        it.selected = false
                        if (clearSearchResult) {
                            it.isSearchResult = false
                            textPage.searchResult.remove(it)
                        }
                    }
                }
            }
        }
        invalidate()
        callBack.onCancelSelect()
    }

    fun getSelectedText(): String {
        val textPos = TextPos(0, 0, 0)
        val builder = StringBuilder()
        for (relativePos in selectStart.relativePagePos..selectEnd.relativePagePos) {
            val textPage = relativePage(relativePos)
            textPos.relativePagePos = relativePos
            textPage.lines.forEachIndexed { lineIndex, textLine ->
                textPos.lineIndex = lineIndex
                textLine.columns.forEachIndexed { charIndex, column ->
                    textPos.columnIndex = charIndex
                    val compareStart = textPos.compare(selectStart)
                    val compareEnd = textPos.compare(selectEnd)
                    if (column is TextColumn) {
                        when {
                            compareStart == 0 -> {
                                if (selectStart.isTouch) {
                                    builder.append(column.charData)
                                }
                                if (
                                    textLine.isParagraphEnd
                                    && charIndex == textLine.charSize - 1
                                    && compareEnd != 0
                                ) {
                                    builder.append("\n")
                                }
                            }

                            compareEnd == 0 -> if (selectEnd.isTouch || selectEnd.isLast) {
                                builder.append(column.charData)
                            }

                            compareStart > 0 && compareEnd < 0 -> {
                                builder.append(column.charData)
                                if (
                                    textLine.isParagraphEnd
                                    && charIndex == textLine.charSize - 1
                                ) {
                                    builder.append("\n")
                                }
                            }
                        }
                    }
                }
            }
        }
        return builder.toString()
    }


    private fun relativeOffset(relativePos: Int): Float {
        return when (relativePos) {
            0 -> pageOffset.toFloat()
            1 -> pageOffset + textPage.height
            else -> pageOffset + textPage.height + pageFactory.nextPage.height
        }
    }

    fun relativePage(relativePos: Int): TextPage {
        return when (relativePos) {
            0 -> textPage
            1 -> pageFactory.nextPage
            else -> pageFactory.nextPlusPage
        }
    }

    override fun canScrollVertically(direction: Int): Boolean {
        return callBack.isScroll && pageFactory.hasNext()
    }

    override fun dispatchTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                longScreenshot = true
                scrollY = 0
            }

            MotionEvent.ACTION_UP -> {
                longScreenshot = false
                scrollY = 0
            }
        }
        return callBack.onLongScreenshotTouchEvent(event)
    }

    interface CallBack {
        val headerHeight: Int
        val pageFactory: TextPageFactory
        val isScroll: Boolean
        var isSelectingSearchResult: Boolean
        fun upSelectedStart(x: Float, y: Float, top: Float)
        fun upSelectedEnd(x: Float, y: Float)
        fun onImageLongPress(x: Float, y: Float, src: String)
        fun onCancelSelect()
        fun onLongScreenshotTouchEvent(event: MotionEvent): Boolean
    }
}
