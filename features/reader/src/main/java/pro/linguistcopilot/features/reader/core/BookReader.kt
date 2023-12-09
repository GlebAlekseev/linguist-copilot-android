package pro.linguistcopilot.features.reader.core

import android.net.Uri
import android.os.ParcelFileDescriptor
import android.text.TextUtils
import pro.linguistcopilot.features.reader.domain.BookChapter
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Metadata
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.TOCReference
import me.ag2s.epublib.epub.EpubReader
import me.ag2s.epublib.util.StringUtil
import me.ag2s.epublib.util.zip.AndroidZipFile
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.linguistcopilot.features.reader.di.scope.ReaderComponentScope
import pro.linguistcopilot.features.reader.domain.Book
import pro.linguistcopilot.features.reader.domain.BookUrlArg
import java.io.IOException
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.Charset
import javax.inject.Inject

@ReaderComponentScope
class BookReader @Inject constructor(
    private val bookUrlArg: BookUrlArg
) {
    private val bookUrl get() = bookUrlArg.bookUrl
    private val mCharset: Charset = Charset.defaultCharset()
    private var fileDescriptor: ParcelFileDescriptor? = null
    private val coverUrl: String by lazy {
        EpubUtils.getCoverPath(bookUrl)
    }
    private val epubBook: EpubBook? by lazy {
        val localUri = localParseUri(bookUrl)
        val epubBook = readEpub(localUri)
        epubBook?.saveCompressedCover(coverUrl)
        return@lazy epubBook
    }
    private val metadata: Metadata? by lazy {
        epubBook?.metadata
    }
    private val descriptions: List<String>? by lazy {
        metadata?.descriptions
    }

    private val intro: String? by lazy {
        if (!descriptions.isNullOrEmpty()) {
            val desc = descriptions!![0]
            if (desc.isXml()) {
                Jsoup.parse(desc).text()
            } else {
                desc
            }
        } else null
    }

    private val name: String by lazy {
        val name = metadata?.firstTitle
        if (name.isNullOrEmpty()) FileUtils.getFileName(bookUrl)
        else name
    }

    private val author: String? by lazy {
        metadata?.authors?.getOrNull(0).toString().replace("^, |, $".toRegex(), "")
    }

    val book: Book by lazy {
        Book(
            url = bookUrl,
            coverUrl = coverUrl,
            name = name,
            intro = intro,
            author = author
        )
    }

    private fun readEpub(uri: Uri): EpubBook? {
        val parcelFileDescriptor = FileUtils.getParcelFileDescriptor(uri)
            ?: return null
        fileDescriptor = parcelFileDescriptor
        val zipFile = AndroidZipFile(
            parcelFileDescriptor,
            FileUtils.getFileName(uri.toString())
        )
        return EpubReader().readEpubLazy(zipFile, "utf-8")
    }

    fun getChapters(): List<BookChapter> {
        val chapters = mutableListOf<BookChapter>()
        val eBook = epubBook ?: return chapters
        val refs = eBook.tableOfContents.tocReferences
        if (refs == null || refs.isEmpty()) {
            val spineReferences = eBook.spine.spineReferences
            var i = 0
            val size = spineReferences.size
            while (i < size) {
                val resource = spineReferences[i].resource
                var title = resource.title
                if (TextUtils.isEmpty(title)) {
                    try {
                        val doc =
                            Jsoup.parse(String(resource.data, mCharset))
                        val elements = doc.getElementsByTag("title")
                        if (elements.size > 0) {
                            title = elements[0].text()
                        }
                    } catch (e: IOException) {
                        e.printStackTrace()
                    }
                }
                val chapter = BookChapter(
                    url = resource.href,
                    index = i,
                    bookUrl = bookUrl,
                    title = if (i == 0 && title.isEmpty()) "Обложка" else title
                )
                chapters.lastOrNull()?.nextUrl = chapter.url
                chapters.add(chapter)
                i++
            }
        } else {
            parseFirstPage(chapters, refs)
            parseMenu(chapters, refs)

            for (i in chapters.indices) {
                chapters[i].index = i
            }
        }
        return chapters
    }

    private fun parseFirstPage(
        chapters: MutableList<BookChapter>,
        refs: List<TOCReference>?
    ) {
        val contents = epubBook?.contents
        if (epubBook == null || contents == null || refs == null) return
        val firstRef = refs.firstOrNull { it.resource != null } ?: return
        var i = 0
        while (i < contents.size) {
            val content = contents[i]
            if (!content.mediaType.toString().contains("htm")) {
                i++
                continue
            }
            if (firstRef.completeHref.substringBeforeLast("#") == content.href) break
            var title = content.title
            if (TextUtils.isEmpty(title)) {
                val elements = Jsoup.parse(
                    String(epubBook!!.resources.getByHref(content.href).data, mCharset)
                ).getElementsByTag("title")
                title =
                    if (elements.size > 0 && elements[0].text().isNotBlank())
                        elements[0].text()
                    else
                        "--Том 1--"
            }
            val chapter = BookChapter(
                url = content.href,
                title = title,
                bookUrl = bookUrl,
                startFragmentId = if (content.href.substringAfter("#") == content.href) null
                else content.href.substringAfter("#"),
            )
            chapters.lastOrNull()?.endFragmentId = chapter.startFragmentId
            chapters.lastOrNull()?.nextUrl = chapter.url
            chapters.add(chapter)
            i++
        }
    }

    private fun parseMenu(
        chapters: MutableList<BookChapter>,
        refs: List<TOCReference>?
    ) {
        refs?.forEach { ref ->
            if (ref.resource != null) {
                val chapter = BookChapter(
                    bookUrl = bookUrl,
                    title = ref.title,
                    url = ref.completeHref,
                    startFragmentId = ref.fragmentId
                )
                chapters.lastOrNull()?.endFragmentId = chapter.startFragmentId
                chapters.lastOrNull()?.nextUrl = chapter.url
                chapters.add(chapter)
            }
            if (ref.children != null && ref.children.isNotEmpty()) {
                chapters.lastOrNull()?.isVolume = true
                parseMenu(chapters, ref.children)
            }
        }
    }

    fun getImage(href: String): InputStream? {
        if (href == "cover.jpeg") return epubBook?.coverImage?.inputStream
        val abHref = URLDecoder.decode(href.replace("../", ""), "UTF-8")
        return epubBook?.resources?.getByHref(abHref)?.inputStream
    }

    fun getContent(chapter: BookChapter): String? {
        if (chapter.url.contains("titlepage.xhtml") ||
            chapter.url.contains("cover")
        ) {
            return "<img src=\"cover.jpeg\" />"
        }
        val contents = epubBook?.contents ?: return null
        val nextChapterFirstResourceHref = chapter.nextUrl?.substringBeforeLast("#")
        val currentChapterFirstResourceHref = chapter.url.substringBeforeLast("#")
        val isLastChapter = nextChapterFirstResourceHref.isNullOrBlank()
        val startFragmentId = chapter.startFragmentId
        val endFragmentId = chapter.endFragmentId
        val elements = Elements()
        var findChapterFirstSource = false
        val includeNextChapterResource = !endFragmentId.isNullOrBlank()
        for (res in contents) {
            if (!findChapterFirstSource) {
                if (currentChapterFirstResourceHref != res.href) continue
                findChapterFirstSource = true
                elements.add(
                    getBody(res, startFragmentId, endFragmentId)
                )
                if (!isLastChapter && res.href == nextChapterFirstResourceHref) break
                continue
            }
            if (nextChapterFirstResourceHref != res.href) {
                elements.add(getBody(res, null, null))
            } else {
                if (includeNextChapterResource) {
                    elements.add(getBody(res, null, endFragmentId))
                }
                break
            }
        }
        elements.select("title").remove()
        elements.select("img").forEach {
            val src = it.attr("src")
            val path = chapter.url.substringBeforeLast("/", "")
            val absSrc = if (path.isEmpty()) {
                src
            } else {
                StringUtil.collapsePathDots("$path/$src")
            }
            it.attr("src", absSrc)
        }
        return HtmlFormatter.formatKeepImg(elements.outerHtml())
    }

    private fun getBody(res: Resource, startFragmentId: String?, endFragmentId: String?): Element {
        var bodyElement = Jsoup.parse(String(res.data, mCharset)).body()
        bodyElement.children().run {
            select("script").remove()
            select("style").remove()
        }
        var bodyString = bodyElement.outerHtml()
        val originBodyString = bodyString
        if (!startFragmentId.isNullOrBlank()) {
            bodyElement.getElementById(startFragmentId)?.outerHtml()?.let {
                bodyString = bodyString.substringAfter(it).ifBlank { bodyString }
            }
        }
        if (!endFragmentId.isNullOrBlank() && endFragmentId != startFragmentId) {
            bodyElement.getElementById(endFragmentId)?.outerHtml()?.let {
                bodyString = bodyString.substringBefore(it)
            }
        }
        if (bodyString != originBodyString) {
            bodyElement = Jsoup.parse(bodyString).body()
        }
        return bodyElement
    }

    protected fun finalize() {
        fileDescriptor?.close()
    }
}