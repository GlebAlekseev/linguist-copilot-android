package pro.linguistcopilot.features.reader.presentation.view.utils

import android.text.TextPaint

val TextPaint.textHeight: Float
    get() = fontMetrics.run { descent - ascent + leading }
