package pro.linguistcopilot.features.reader.presentation.view.entities.column

interface BaseColumn {
    var start: Float
    var end: Float

    fun isTouch(x: Float): Boolean {
        return x > start && x < end
    }
}