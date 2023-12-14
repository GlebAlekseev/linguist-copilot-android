package pro.linguistcopilot.features.reader.presentation.view.delegate

import android.content.Context
import android.graphics.Canvas
import android.view.MotionEvent
import android.view.animation.LinearInterpolator
import android.widget.Scroller
import androidx.annotation.CallSuper
import com.google.android.material.snackbar.Snackbar
import pro.linguistcopilot.features.reader.presentation.view.PageView
import pro.linguistcopilot.features.reader.presentation.view.ReadView
import pro.linguistcopilot.features.reader.presentation.view.entities.PageDirection
import kotlin.math.abs


abstract class PageDelegate(protected val readView: ReadView) {

    protected val context: Context = readView.context
    protected val startX: Float get() = readView.startX
    protected val startY: Float get() = readView.startY
    protected val lastX: Float get() = readView.lastX
    protected val lastY: Float get() = readView.lastY
    protected val touchX: Float get() = readView.touchX
    protected val touchY: Float get() = readView.touchY

    protected val nextPage: PageView get() = readView.nextPage
    protected val curPage: PageView get() = readView.curPage
    protected val prevPage: PageView get() = readView.prevPage

    protected var viewWidth: Int = readView.width
    protected var viewHeight: Int = readView.height

    protected val scroller: Scroller by lazy {
        Scroller(readView.context, LinearInterpolator())
    }

    private val snackBar: Snackbar by lazy {
        Snackbar.make(readView, "", Snackbar.LENGTH_SHORT)
    }

    var isMoved = false
    var noNext = true

    var mDirection = PageDirection.NONE
    var isCancel = false
    var isRunning = false
    var isStarted = false

    private var selectedOnDown = false

    init {
        curPage.resetPageOffset()
    }

    open fun fling(
        startX: Int, startY: Int, velocityX: Int, velocityY: Int,
        minX: Int, maxX: Int, minY: Int, maxY: Int
    ) {
        scroller.fling(startX, startY, velocityX, velocityY, minX, maxX, minY, maxY)
        isRunning = true
        isStarted = true
        readView.invalidate()
    }

    protected fun startScroll(startX: Int, startY: Int, dx: Int, dy: Int, animationSpeed: Int) {
        val duration = if (dx != 0) {
            (animationSpeed * abs(dx)) / viewWidth
        } else {
            (animationSpeed * abs(dy)) / viewHeight
        }
        scroller.startScroll(startX, startY, dx, dy, duration)
        isRunning = true
        isStarted = true
        readView.invalidate()
    }

    protected fun stopScroll() {
        isStarted = false
        readView.post {
            isMoved = false
            isRunning = false
            readView.invalidate()
        }
    }

    open fun setViewSize(width: Int, height: Int) {
        viewWidth = width
        viewHeight = height
    }

    fun scroll() {
        if (scroller.computeScrollOffset()) {
            readView.setTouchPoint(scroller.currX.toFloat(), scroller.currY.toFloat())
        } else if (isStarted) {
            onAnimStop()
            stopScroll()
        }
    }

    open fun onScroll() = Unit

    abstract fun abortAnim()

    abstract fun onAnimStart(animationSpeed: Int)

    abstract fun onDraw(canvas: Canvas)

    abstract fun onAnimStop()

    abstract fun nextPageByAnim(animationSpeed: Int)

    abstract fun prevPageByAnim(animationSpeed: Int)

    open fun keyTurnPage(direction: PageDirection) {
        if (isRunning) return
        when (direction) {
            PageDirection.NEXT -> nextPageByAnim(100)
            PageDirection.PREV -> prevPageByAnim(100)
            else -> return
        }
    }

    @CallSuper
    open fun setDirection(direction: PageDirection) {
        mDirection = direction
    }

    abstract fun onTouch(event: MotionEvent)

    fun onDown() {
        isMoved = false
        noNext = false
        isRunning = false
        isCancel = false
        setDirection(PageDirection.NONE)
    }

    fun hasPrev(): Boolean {
        val hasPrev = readView.pageFactory?.hasPrev() ?: false
        if (!hasPrev) {
            if (!snackBar.isShown) {
                snackBar.setText("R.string.no_prev_page")
                snackBar.show()
            }
        }
        return hasPrev
    }

    fun hasNext(): Boolean {
        val hasNext = readView.pageFactory?.hasNext() ?: false
        if (!hasNext) {
            if (!snackBar.isShown) {
                snackBar.setText("R.string.no_next_page")
                snackBar.show()
            }
        }
        return hasNext
    }

    fun dismissSnackBar() {
        if (snackBar.isShown) {
            snackBar.dismiss()
        }
    }

    open fun onDestroy() {

    }
}
