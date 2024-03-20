package pro.linguistcopilot.feature.word.entity

sealed class Language {
    data object English : Language()
    data object Russian : Language()
    data object Auto : Language()
    data class Other(val code: String) : Language()
}