package pro.linguistcopilot.feature.bookDownload.entity

data class ProgressResult(
    val progress: Int,
    val toastMessage: String? = null
)