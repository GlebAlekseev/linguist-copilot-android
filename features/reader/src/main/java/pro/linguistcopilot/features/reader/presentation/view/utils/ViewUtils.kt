package pro.linguistcopilot.features.reader.presentation.view.utils

import android.content.Context
import android.graphics.Color
import android.view.View
import androidx.annotation.ColorRes
import androidx.core.content.ContextCompat

fun View.invisible() {
    if (visibility != View.INVISIBLE) {
        visibility = View.INVISIBLE
    }
}

fun Context.getCompatColor(@ColorRes id: Int): Int = ContextCompat.getColor(this, id)

val Context.accentColor: Int
    get() = Color.MAGENTA