package pro.linguistcopilot.feature.textProcessing.entity

sealed class Lang {
    data object EN : Lang()
    data object UNKNOWN : Lang()
}