package pro.linguistcopilot.features.reader.presentation.view3


data class BookContent(
    val sameTitleRemoved: Boolean,
    val textList: List<String>
) {

    override fun toString(): String {
        return textList.joinToString("\n")
    }

}
