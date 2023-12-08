package pro.linguistcopilot.features.reader.core.book

import android.content.Context
import java.io.File

val Context.externalFiles: File
    get() = this.getExternalFilesDir(null) ?: this.filesDir


object RuleBigDataHelp {

    suspend fun clearInvalid() {

    }

    fun putBookVariable(bookUrl: String, key: String, value: String?) {

    }

    fun getBookVariable(bookUrl: String, key: String?): String? {
        return null
    }


    fun putChapterVariable(bookUrl: String, chapterUrl: String, key: String, value: String?) {

    }

    fun getChapterVariable(bookUrl: String, chapterUrl: String, key: String): String? {
        return null
    }

    fun putRssVariable(origin: String, link: String, key: String, value: String?) {

    }

    fun getRssVariable(origin: String, link: String, key: String): String? {
        return null
    }
}