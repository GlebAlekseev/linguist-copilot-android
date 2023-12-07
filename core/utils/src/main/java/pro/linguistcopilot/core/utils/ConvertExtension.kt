package pro.linguistcopilot.core.utils

import android.content.res.Resources

fun Int.dpToPx(): Int = this.toFloat().dpToPx().toInt()

fun Int.spToPx(): Int = this.toFloat().spToPx().toInt()

fun Float.dpToPx(): Float = android.util.TypedValue.applyDimension(
    android.util.TypedValue.COMPLEX_UNIT_DIP, this, Resources.getSystem().displayMetrics
)

fun Float.spToPx(): Float = android.util.TypedValue.applyDimension(
    android.util.TypedValue.COMPLEX_UNIT_SP, this, Resources.getSystem().displayMetrics
)