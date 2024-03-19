package pro.linguistcopilot.feature.translate.entity

sealed class Language(open val code: String) {
    data class English(override val code: String) : Language(code)
    data class Russian(override val code: String) : Language(code)
}