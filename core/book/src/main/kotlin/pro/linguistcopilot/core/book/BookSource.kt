package pro.linguistcopilot.core.book

import pro.linguistcopilot.core.book.entity.Chapter
import pro.linguistcopilot.core.book.entity.Metadata
import java.io.InputStream

interface BookSource {
    val metadata: Metadata
    val chapters: List<Chapter>
    fun getImage(href: String): InputStream?
    fun getContent(chapter: Chapter): String
}