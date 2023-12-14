package pro.linguistcopilot.features.reader.presentation.view.entities.column

data class ImageColumn(
    override var start: Float,
    override var end: Float,
    var src: String
) : BaseColumn