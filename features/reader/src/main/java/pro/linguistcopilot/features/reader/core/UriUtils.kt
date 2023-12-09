package pro.linguistcopilot.features.reader.core

import android.net.Uri
import java.io.File

fun Uri.isContentScheme() = this.scheme == "content"

fun localParseUri(url: String): Uri {
    val uri = if (url.isUri()) {
        Uri.parse(url)
    } else {
        Uri.fromFile(File(url))
    }
    return uri
}

fun String?.isUri(): Boolean {
    this ?: return false
    return this.startsWith("file://", true) || isContentScheme()
}

fun String?.isContentScheme(): Boolean = this?.startsWith("content://") == true
