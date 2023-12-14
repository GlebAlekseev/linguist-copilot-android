package pro.linguistcopilot.features.reader.presentation.view3.delegate

import android.graphics.Canvas
import pro.linguistcopilot.features.reader.presentation.view3.ReadView

class NoAnimPageDelegate(readView: ReadView) : HorizontalPageDelegate(readView) {

    override fun onAnimStart(animationSpeed: Int) {
        if (!isCancel) {
            readView.fillPage(mDirection)
        }
        stopScroll()
    }

    override fun setBitmap() {
        // nothing
    }

    override fun onDraw(canvas: Canvas) {
        // nothing
    }

    override fun onAnimStop() {
        // nothing
    }


}