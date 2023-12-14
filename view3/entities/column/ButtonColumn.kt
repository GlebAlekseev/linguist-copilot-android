package pro.linguistcopilot.features.reader.presentation.view3.entities.column

import androidx.annotation.Keep


/**
 * 按钮列
 */
@Keep
data class ButtonColumn(
    override var start: Float,
    override var end: Float
) : BaseColumn