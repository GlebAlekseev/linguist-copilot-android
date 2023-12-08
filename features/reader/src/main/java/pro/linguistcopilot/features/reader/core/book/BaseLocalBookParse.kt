package pro.linguistcopilot.features.reader.core.book

import io.legado.app.data.entities.Book
import io.legado.app.data.entities.BookChapter
import java.io.InputStream

/**
 *companion object interface
 *see EpubFile.kt
 */
interface BaseLocalBookParse {

    fun upBookInfo(book: Book,defaultBookDir: String?, importBookDir: String?)

    fun getChapterList(book: Book,defaultBookDir: String?, importBookDir: String?): ArrayList<BookChapter>

    fun getContent(book: Book, chapter: BookChapter,defaultBookDir: String?, importBookDir: String?): String?

    fun getImage(book: Book, href: String,defaultBookDir: String?, importBookDir: String?): InputStream?

}
