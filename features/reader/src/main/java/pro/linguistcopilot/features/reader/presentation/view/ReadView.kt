package pro.linguistcopilot.features.reader.presentation.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.RectF
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.ViewConfiguration
import android.widget.FrameLayout
import pro.linguistcopilot.features.reader.presentation.view.delegate.PageDelegate
import pro.linguistcopilot.features.reader.presentation.view.entities.PageDirection
import pro.linguistcopilot.features.reader.presentation.view.entities.TextPage
import pro.linguistcopilot.features.reader.presentation.view.utils.invisible


class ReadView(context: Context, attrs: AttributeSet) : FrameLayout(context, attrs) {
    val prevPage by lazy { PageView(context) }
    val curPage by lazy { PageView(context) }
    val nextPage by lazy { PageView(context) }

    var callback: Callback? = null
    var pageFactory: PageFactory<TextPage>? = null
        set(value) {
            field = value
            initPageFactoryPageViews()
            invalidate()
        }
    var pageDelegate: PageDelegate? = null
        set(value) {
            field = value
            invalidate()
        }
    var readBook: ReadBook? = null
        set(value) {
            field = value?.also {
                this.pageFactory = TextPageFactory(
                    readView = this,
                    readBook = it
                )
            }
            initReadBookPageViews()
            invalidate()
        }

    fun initReadBookPageViews() {
        prevPage.readBook = readBook
        curPage.readBook = readBook
        nextPage.readBook = readBook
    }

    fun initPageFactoryPageViews() {
        prevPage.pageFactory = pageFactory
        curPage.pageFactory = pageFactory
        nextPage.pageFactory = pageFactory
    }

    var startX: Float = 0f
    var startY: Float = 0f

    var lastX: Float = 0f
    var lastY: Float = 0f
    var touchX: Float = 0f
    var touchY: Float = 0f

    var isAbortAnim = false

    private val slopSquare by lazy { ViewConfiguration.get(context).scaledTouchSlop }
    private var pageSlopSquare: Int = slopSquare
    var pageSlopSquare2: Int = pageSlopSquare * pageSlopSquare

    val defaultAnimationSpeed = 300
    var isScroll = false

    init {
        addView(nextPage)
        addView(curPage)
        addView(prevPage)
        nextPage.invisible()
        prevPage.invisible()
        curPage.markAsMainView()
    }

    private val leftRect = RectF()
    private val rightRect = RectF()

    private fun setRect2x() {
        leftRect.set(0f, 0f, width * 0.33f, height.toFloat())
        rightRect.set(width * 0.66f, 0f, width.toFloat(), height.toFloat())
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        setRect2x()
    }

    override fun dispatchDraw(canvas: Canvas) {
        super.dispatchDraw(canvas)
        pageDelegate?.onDraw(canvas)
    }

    override fun computeScroll() {
        pageDelegate?.scroll()
    }

    override fun onInterceptTouchEvent(ev: MotionEvent?): Boolean {
        return true
    }

    fun setTouchPoint(x: Float, y: Float, invalidate: Boolean = true) {
        lastX = touchX
        lastY = touchY
        touchX = x
        touchY = y
        if (invalidate) {
            invalidate()
        }
        pageDelegate?.onScroll()
    }

    fun setStartPoint(x: Float, y: Float, invalidate: Boolean = true) {
        startX = x
        startY = y
        lastX = x
        lastY = y
        touchX = x
        touchY = y

        if (invalidate) {
            invalidate()
        }
    }

    fun fillPage(direction: PageDirection): Boolean {
        return when (direction) {
            PageDirection.PREV -> {
                pageFactory?.moveToPrev(true) ?: false
            }

            PageDirection.NEXT -> {
                pageFactory?.moveToNext(true) ?: false
            }

            else -> false
        }
    }

    fun refreshContent(relativePosition: Int, resetPageOffset: Boolean) {
        pageFactory?.let { pageFactory ->
            curPage.contentDescription = pageFactory.curPage.text
            if (isScroll) {
                curPage.setContent(pageFactory.curPage, resetPageOffset)
            } else {
                when (relativePosition) {
                    -1 -> prevPage.setContent(pageFactory.prevPage)
                    1 -> nextPage.setContent(pageFactory.nextPage)
                    else -> {
                        curPage.setContent(pageFactory.curPage, resetPageOffset)
                        nextPage.setContent(pageFactory.nextPage)
                        prevPage.setContent(pageFactory.prevPage)
                    }
                }
            }
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.actionMasked == MotionEvent.ACTION_POINTER_DOWN || event.actionMasked == MotionEvent.ACTION_POINTER_UP) {
            pageDelegate?.onTouch(event)
        }
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                setStartPoint(event.x, event.y, false)
            }

            MotionEvent.ACTION_MOVE -> {
            }

            MotionEvent.ACTION_UP -> {
                onSingleTapUp()
            }

            MotionEvent.ACTION_CANCEL -> {
            }
        }
        return true
    }

    private fun onSingleTapUp() {
        when {
            leftRect.contains(startX, startY) -> {
                pageDelegate?.prevPageByAnim(defaultAnimationSpeed)
            }

            rightRect.contains(startX, startY) -> {
                pageDelegate?.nextPageByAnim(defaultAnimationSpeed)
            }
        }
    }

    interface Callback {

    }
}
