package pro.linguistcopilot.features.reader.presentation.view3.entities.column

/**
 * 列基类
 */
interface BaseColumn {
    var start: Float
    var end: Float

    fun isTouch(x: Float): Boolean {
        return x > start && x < end
    }

}