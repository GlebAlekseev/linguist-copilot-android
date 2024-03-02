package pro.linguistcopilot.core.utils

interface ILogger {
    val logs: List<LogRow>

    fun put(message: String, throwable: Throwable? = null, isDisplayToast: Boolean = false)

    data class LogRow(
        val time: Long,
        val message: String,
        val throwable: Throwable?
    )
}

