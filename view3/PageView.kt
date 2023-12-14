package pro.linguistcopilot.features.reader.presentation.view3

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.widget.FrameLayout
import androidx.core.view.isGone
import pro.linguistcopilot.core.utils.activity
import pro.linguistcopilot.features.reader.databinding.ViewBookPageBinding
import pro.linguistcopilot.features.reader.presentation.view3.entities.TextLine
import pro.linguistcopilot.features.reader.presentation.view3.entities.TextPage
import pro.linguistcopilot.features.reader.presentation.view3.entities.TextPos
import splitties.views.backgroundColor
import java.util.*

val Context.statusBarHeight: Int
    @SuppressLint("DiscouragedApi", "InternalInsetResource")
    get() {
        if (Build.BOARD == "windows") {
            return 0
        }
        val resourceId = resources.getIdentifier("status_bar_height", "dimen", "android")
        return resources.getDimensionPixelSize(resourceId)
    }

fun View.gone(gone: Boolean) {
    if (gone) {
        gone()
    } else {
        visibility = View.VISIBLE
    }
}

fun View.gone() {
    if (visibility != View.GONE) {
        visibility = View.GONE
    }
}

/**
 * 页面视图
 */
class PageView(context: Context) : FrameLayout(context) {

    private val binding = ViewBookPageBinding.inflate(LayoutInflater.from(context), this, true)
    private val readBookActivity get() = activity
//    private var battery = 100

    val headerHeight: Int
        get() {
            val h1 = if (true) 0 else context.statusBarHeight
            val h2 = if (binding.llHeader.isGone) 0 else binding.llHeader.height
            return h1 + h2
        }

    init {
        if (!isInEditMode) {
            upStyle()
        }
        binding.contentTextView.upView = {
//            setProgress(it)
        }
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        upBg()
    }

    fun upStyle() = binding.run {
        upTipStyle()
        let {
            val tipColor = 0
            val tipDividerColor = Color.parseColor("#66666666")

//            tvHeaderLeft.setColor(tipColor)
//            tvHeaderMiddle.setColor(tipColor)
//            tvHeaderRight.setColor(tipColor)
//            tvFooterLeft.setColor(tipColor)
//            tvFooterMiddle.setColor(tipColor)
//            tvFooterRight.setColor(tipColor)
            vwTopDivider.backgroundColor = tipDividerColor
            vwBottomDivider.backgroundColor = tipDividerColor
            upStatusBar()
//            llHeader.setPadding(
//                it.headerPaddingLeft.dpToPx(),
//                it.headerPaddingTop.dpToPx(),
//                it.headerPaddingRight.dpToPx(),
//                it.headerPaddingBottom.dpToPx()
//            )
//            llFooter.setPadding(
//                it.footerPaddingLeft.dpToPx(),
//                it.footerPaddingTop.dpToPx(),
//                it.footerPaddingRight.dpToPx(),
//                it.footerPaddingBottom.dpToPx()
//            )
            vwTopDivider.gone(llHeader.isGone || !true)
            vwBottomDivider.gone(llFooter.isGone || !true)
        }
        contentTextView.upVisibleRect()
//        upTime()
//        upBattery(battery)
    }

    /**
     * 显示状态栏时隐藏header
     */
    fun upStatusBar() = with(binding.vwStatusBar) {
        setPadding(paddingLeft, context.statusBarHeight, paddingRight, paddingBottom)
        isGone = true
    }

    /**
     * 更新阅读信息
     */
    private fun upTipStyle() = binding.run {
//        tvHeaderLeft.tag = null
//        tvHeaderMiddle.tag = null
//        tvHeaderRight.tag = null
//        tvFooterLeft.tag = null
//        tvFooterMiddle.tag = null
//        tvFooterRight.tag = null
        llHeader.isGone = when (0) {
            1 -> false
            2 -> true
            else -> !false
        }
        llFooter.isGone = when (0) {
            1 -> true
            else -> false
        }
//        ReadTipConfig.apply {
//            tvHeaderLeft.isGone = tipHeaderLeft == none
//            tvHeaderRight.isGone = tipHeaderRight == none
//            tvHeaderMiddle.isGone = tipHeaderMiddle == none
//            tvFooterLeft.isInvisible = tipFooterLeft == none
//            tvFooterRight.isGone = tipFooterRight == none
//            tvFooterMiddle.isGone = tipFooterMiddle == none
//        }
//        tvTitle = getTipView(ReadTipConfig.chapterTitle)?.apply {
//            tag = ReadTipConfig.chapterTitle
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvTime = getTipView(ReadTipConfig.time)?.apply {
//            tag = ReadTipConfig.time
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvBattery = getTipView(ReadTipConfig.battery)?.apply {
//            tag = ReadTipConfig.battery
//            isBattery = true
//            textSize = 11f
//        }
//        tvPage = getTipView(ReadTipConfig.page)?.apply {
//            tag = ReadTipConfig.page
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvTotalProgress = getTipView(ReadTipConfig.totalProgress)?.apply {
//            tag = ReadTipConfig.totalProgress
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvTotalProgress1 = getTipView(ReadTipConfig.totalProgress1)?.apply {
//            tag = ReadTipConfig.totalProgress1
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvPageAndTotal = getTipView(ReadTipConfig.pageAndTotal)?.apply {
//            tag = ReadTipConfig.pageAndTotal
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvBookName = getTipView(ReadTipConfig.bookName)?.apply {
//            tag = ReadTipConfig.bookName
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvTimeBattery = getTipView(ReadTipConfig.timeBattery)?.apply {
//            tag = ReadTipConfig.timeBattery
//            isBattery = true
//            typeface = ChapterProvider.typeface
//            textSize = 11f
//        }
//        tvBatteryP = getTipView(ReadTipConfig.batteryPercentage)?.apply {
//            tag = ReadTipConfig.batteryPercentage
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
//        tvTimeBatteryP = getTipView(ReadTipConfig.timeBatteryPercentage)?.apply {
//            tag = ReadTipConfig.timeBatteryPercentage
//            isBattery = false
//            typeface = ChapterProvider.typeface
//            textSize = 12f
//        }
    }

    /**
     * 获取信息视图
     * @param tip 信息类型
     */


    /**
     * 更新背景
     */
    fun upBg() {
        binding.vwRoot.backgroundColor = 0
        binding.vwBg.background = null
        upBgAlpha()
    }

    /**
     * 更新背景透明度
     */
    fun upBgAlpha() {
        binding.vwBg.alpha = 100 / 100f
    }


    /**
     * 设置内容
     */
    fun setContent(textPage: TextPage, resetPageOffset: Boolean = true) {
        if (resetPageOffset) {
            resetPageOffset()
        }
        binding.contentTextView.setContent(textPage)
    }

    /**
     * 设置无障碍文本
     */
    fun setContentDescription(content: String) {
        binding.contentTextView.contentDescription = content
    }

    /**
     * 重置滚动位置
     */
    fun resetPageOffset() {
        binding.contentTextView.resetPageOffset()
    }

    /**
     * 设置进度
     */
    /**
     * 滚动事件
     */
    fun scroll(offset: Int) {
        binding.contentTextView.scroll(offset)
    }

    /**
     * 更新是否开启选择功能
     */
    fun upSelectAble(selectAble: Boolean) {
        binding.contentTextView.selectAble = selectAble
    }

    /**
     * 优先处理页面内单击
     * @return true:已处理, false:未处理
     */
    fun onClick(x: Float, y: Float): Boolean {
        return binding.contentTextView.click(x, y - headerHeight)
    }

    /**
     * 长按事件
     */
    fun longPress(
        x: Float, y: Float,
        select: (textPos: TextPos) -> Unit,
    ) {
        return binding.contentTextView.longPress(x, y - headerHeight, select)
    }

    /**
     * 选择文本
     */
    fun selectText(
        x: Float, y: Float,
        select: (textPos: TextPos) -> Unit,
    ) {
        return binding.contentTextView.selectText(x, y - headerHeight, select)
    }

    fun getCurVisiblePage(): TextPage {
        return binding.contentTextView.getCurVisiblePage()
    }

    fun getCurVisibleFirstLine(): TextLine? {
        return binding.contentTextView.getCurVisibleFirstLine()
    }

    fun markAsMainView() {
        binding.contentTextView.isMainView = true
    }

    fun selectStartMove(x: Float, y: Float) {
        binding.contentTextView.selectStartMove(x, y - headerHeight)
    }

    fun selectStartMoveIndex(
        relativePagePos: Int,
        lineIndex: Int,
        charIndex: Int,
        isTouch: Boolean = true,
        isLast: Boolean = false
    ) {
        binding.contentTextView.selectStartMoveIndex(
            relativePagePos,
            lineIndex,
            charIndex,
            isTouch,
            isLast
        )
    }

    fun selectStartMoveIndex(textPos: TextPos) {
        binding.contentTextView.selectStartMoveIndex(textPos)
    }

    fun selectEndMove(x: Float, y: Float) {
        binding.contentTextView.selectEndMove(x, y - headerHeight)
    }

    fun selectEndMoveIndex(
        relativePagePos: Int,
        lineIndex: Int,
        charIndex: Int,
        isTouch: Boolean = true,
        isLast: Boolean = false
    ) {
        binding.contentTextView.selectEndMoveIndex(
            relativePagePos,
            lineIndex,
            charIndex,
            isTouch,
            isLast
        )
    }

    fun selectEndMoveIndex(textPos: TextPos) {
        binding.contentTextView.selectEndMoveIndex(textPos)
    }

    fun getReverseStartCursor(): Boolean {
        return binding.contentTextView.reverseStartCursor
    }

    fun getReverseEndCursor(): Boolean {
        return binding.contentTextView.reverseEndCursor
    }

    fun resetReverseCursor() {
        binding.contentTextView.resetReverseCursor()
    }

    fun cancelSelect(clearSearchResult: Boolean = false) {
        binding.contentTextView.cancelSelect(clearSearchResult)
    }

    fun relativePage(relativePagePos: Int): TextPage {
        return binding.contentTextView.relativePage(relativePagePos)
    }

    val textPage get() = binding.contentTextView.textPage

    val selectedText: String get() = binding.contentTextView.getSelectedText()

    val selectStartPos get() = binding.contentTextView.selectStart
}