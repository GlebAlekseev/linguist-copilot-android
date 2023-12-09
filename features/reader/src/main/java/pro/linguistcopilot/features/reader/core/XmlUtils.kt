package pro.linguistcopilot.features.reader.core

fun String?.isXml(): Boolean =
    this?.run {
        val str = this.trim()
        str.startsWith("<") && str.endsWith(">")
    } ?: false