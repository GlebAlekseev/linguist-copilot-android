package pro.linguistcopilot

import android.content.Context
import android.util.Log
import android.widget.Toast
import pro.linguistcopilot.core.utils.ILogger
import pro.linguistcopilot.core.utils.di.ApplicationContext
import javax.inject.Inject

class AppLogger @Inject constructor(
    @ApplicationContext private val context: Context
) : ILogger {
    private val mLogs = mutableListOf<ILogger.LogRow>()

    override val logs: List<ILogger.LogRow>
        get() = mLogs

    override fun put(message: String, throwable: Throwable?, isDisplayToast: Boolean) {
        if (isDisplayToast) {
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
        if (mLogs.size > MAX_BUFFER) {
            mLogs.removeLastOrNull()
        }
        mLogs.add(
            0, ILogger.LogRow(
                time = System.currentTimeMillis(),
                message = message,
                throwable = throwable
            )
        )
        if (BuildConfig.DEBUG) {
            val stackTrace = Thread.currentThread().stackTrace
            if (throwable != null) {
                Log.e(stackTrace[3].className, message, throwable)
            } else {
                Log.d(stackTrace[3].className, message, throwable)
            }
        }
    }

    companion object {
        const val MAX_BUFFER = 1000
    }
}