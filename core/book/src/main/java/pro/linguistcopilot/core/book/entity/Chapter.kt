package pro.linguistcopilot.core.book.entity

data class Chapter(
    val index: Int,
    val href: String,
    val title: String,
    var startFragmentId: String? = null,
    var endFragmentId: String? = null,
    var isVolume: Boolean = false,
)
