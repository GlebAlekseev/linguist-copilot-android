package pro.linguistcopilot.core.python

import android.content.Context
import com.chaquo.python.Python
import com.chaquo.python.android.AndroidPlatform

fun Context.startPython() {
    if (!Python.isStarted()) {
        Python.start(AndroidPlatform(this))
    }
}
