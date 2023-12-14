package pro.linguistcopilot.features.reader.presentation.view.utils

fun String.splitNotBlank(vararg delimiter: String, limit: Int = 0): Array<String> = run {
    this.split(*delimiter, limit = limit).map { it.trim() }.filterNot { it.isBlank() }
        .toTypedArray()
}

fun CharSequence.toStringArray(): Array<String> {
    var codePointIndex = 0
    return try {
        Array(Character.codePointCount(this, 0, length)) {
            val start = codePointIndex
            codePointIndex = Character.offsetByCodePoints(this, start, 1)
            substring(start, codePointIndex)
        }
    } catch (e: Exception) {
        split("").toTypedArray()
    }
}