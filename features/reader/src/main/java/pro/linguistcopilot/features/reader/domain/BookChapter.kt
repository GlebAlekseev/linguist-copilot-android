package pro.linguistcopilot.features.reader.domain


data class BookChapter(
    val url: String,
    val title: String,
    val bookUrl: String,
    val startFragmentId: String? = null,
    var isVolume: Boolean = false,
    var endFragmentId: String? = null,
    var index: Int = 0,
    var nextUrl: String? = null
)