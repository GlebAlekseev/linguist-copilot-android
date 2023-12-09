package pro.linguistcopilot.features.reader.core

import splitties.init.appCtx

object EpubUtils {
    fun getCoverPath(bookUrl: String): String {
        return FileUtils.getPath(
            appCtx.externalFiles,
            "covers",
            "${MD5Utils.md5Encode16(bookUrl)}.jpg"
        )
    }
}