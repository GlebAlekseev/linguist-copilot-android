package pro.linguistcopilot.features.reader.presentation.view.entities.column

data class TextColumn(
    override var start: Float,
    override var end: Float,
    val charData: String,
    var selected: Boolean = false,
    var isSearchResult: Boolean = false
) : BaseColumn