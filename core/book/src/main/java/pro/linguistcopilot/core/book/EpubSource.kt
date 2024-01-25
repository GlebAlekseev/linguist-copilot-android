package pro.linguistcopilot.core.book

import android.net.Uri
import android.os.ParcelFileDescriptor
import me.ag2s.epublib.domain.EpubBook
import me.ag2s.epublib.domain.Resource
import me.ag2s.epublib.domain.TOCReference
import me.ag2s.epublib.epub.EpubReader
import me.ag2s.epublib.util.StringUtil
import me.ag2s.epublib.util.zip.AndroidZipFile
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import org.jsoup.select.Elements
import pro.linguistcopilot.core.book.entity.Chapter
import pro.linguistcopilot.core.book.entity.Metadata
import pro.linguistcopilot.core.book.util.FileUtils
import pro.linguistcopilot.core.book.util.HtmlFormatter
import pro.linguistcopilot.core.book.util.externalFiles
import pro.linguistcopilot.core.book.util.saveCompressedCover
import pro.linguistcopilot.core.utils.MD5Utils.md5Encode16
import splitties.init.appCtx
import java.io.File
import java.io.InputStream
import java.net.URLDecoder
import java.nio.charset.Charset


internal class EpubSource(private val uri: Uri) : BookSource {
    private var fileDescriptor: ParcelFileDescriptor? = null
    private val charset: Charset = Charset.defaultCharset()

    private val fileName by lazy {
        return@lazy uri.lastPathSegment.toString()
    }

    private val coverImageUri: Uri by lazy {
        return@lazy Uri.fromFile(
            File(
                FileUtils.getPath(
                    appCtx.externalFiles,
                    "covers",
                    "${md5Encode16(uri.path!!)}.jpg"
                )
            )
        )
    }

    private val epubBook: EpubBook by lazy {
        readEpub(uri).also {
            it.saveCompressedCover(coverImageUri)
        }
    }

    override val metadata: Metadata by lazy {
        val epub = epubBook
        return@lazy Metadata(
            title = if (epubBook.metadata.firstTitle.isNullOrEmpty()) fileName else epub.metadata.firstTitle,
            titles = epubBook.metadata.titles,
            authors = epubBook.metadata?.authors?.map { "${it.lastname} ${it.firstname}" }
                ?: listOf(),
            descriptions = epubBook.metadata?.descriptions ?: listOf(),
            language = epubBook.metadata.language,
            publishers = epubBook.metadata.publishers,
            subjects = epubBook.metadata.subjects,
            format = epubBook.metadata.format,
            dates = epubBook.metadata.dates.map { it.value },
            uri = uri,
            coverImageUri = coverImageUri
        )
    }

    override val chapters: List<Chapter> by lazy {
        val chapters = mutableListOf<Chapter>()
        val refs = epubBook.tableOfContents.tocReferences
        if (refs.isNullOrEmpty()) {
            parseSpineReferences(chapters)
        } else {
            parseTocReferences(chapters)
        }
        return@lazy chapters
    }

    val Chapter.nextHref: String? get() = chapters.getOrNull(this.index + 1)?.href

    private fun parseSpineReferences(chapters: MutableList<Chapter>) {
        val spineReferences = epubBook.spine.spineReferences
        for ((index, spineReference) in spineReferences.withIndex()) {
            val resource = spineReference.resource
            val title = if (resource.title.isNullOrEmpty()) {
                val doc = Jsoup.parse(String(resource.data, charset))
                val elements = doc.getElementsByTag("title")
                if (elements.size > 0) elements[0].text() else resource.title
            } else resource.title
            val chapter = Chapter(
                index = index,
                href = resource.href,
                title = title,
            )
            chapters.add(chapter)
        }
    }

    private fun parseTocReferences(chapters: MutableList<Chapter>) {
        parseTocReferenceFirstPage(chapters)
        parseTocReferenceMenu(chapters)
    }

    private fun parseTocReferenceFirstPage(chapters: MutableList<Chapter>) {
        val firstReference =
            epubBook.tableOfContents.tocReferences.firstOrNull { it.resource != null }
        for (content in epubBook.contents) {
            if (!content.mediaType.toString().contains("htm")) continue
            if (firstReference?.completeHref?.substringBeforeLast("#") == content.href) break
            val title = if (content.title.isNullOrEmpty()) {
                val elements = Jsoup.parse(
                    String(epubBook.resources.getByHref(content.href).data, charset)
                ).getElementsByTag("title")
                if (elements.size > 0 && elements[0].text().isNotBlank())
                    elements[0].text()
                else
                    "--Том 1--"
            } else content.title
            val chapter = Chapter(
                index = chapters.size,
                href = content.href,
                title = title,
                startFragmentId = if (content.href.substringAfter("#") == content.href) null
                else content.href.substringAfter("#")
            )
            chapters.lastOrNull()?.endFragmentId = chapter.startFragmentId
            chapters.add(chapter)
        }
    }

    private fun parseTocReferenceMenu(
        chapters: MutableList<Chapter>,
        refs: List<TOCReference> = epubBook.tableOfContents.tocReferences
    ) {
        for (ref in refs) {
            if (ref.resource != null) {
                val chapter = Chapter(
                    title = ref.title,
                    href = ref.completeHref,
                    startFragmentId = ref.fragmentId,
                    index = chapters.size
                )
                chapters.lastOrNull()?.endFragmentId = chapter.startFragmentId
                chapters.add(chapter)
            }
            if (!ref.children.isNullOrEmpty()) {
                chapters.lastOrNull()?.isVolume = true
                parseTocReferenceMenu(chapters, ref.children)
            }
        }
    }

    override fun getImage(href: String): InputStream? {
        if (href == "cover.jpeg") return epubBook.coverImage.inputStream
        val abHref = URLDecoder.decode(href.replace("../", ""), "UTF-8")
        return epubBook.resources.getByHref(abHref).inputStream
    }

    override fun getContent(chapter: Chapter): String {
        if (chapter.href.contains("titlepage.xhtml") ||
            chapter.href.contains("cover")
        ) return "<img src=\"cover.jpeg\" />"

        val contents = epubBook.contents
        val nextChapterHref = chapter.nextHref?.let {
            it.split("#")[0]
        }
        val currentChapterHref = chapter.href.split("#")[0]
        val isLastChapter = nextChapterHref == null
        val startFragmentId = chapter.startFragmentId
        val endFragmentId = chapter.endFragmentId

        val elements = Elements()
        var findChapterFirstSource = false
        val includeNextChapterResource = !endFragmentId.isNullOrBlank()
        for (content in contents) {
            if (!findChapterFirstSource) {
                if (currentChapterHref != content.href) continue
                findChapterFirstSource = true
                elements.add(getBody(content, startFragmentId, endFragmentId))
                if (!isLastChapter && content.href == nextChapterHref) break
                continue
            }
            if (nextChapterHref != content.href) {
                elements.add(getBody(content, null, null))
            } else {
                if (includeNextChapterResource) {
                    elements.add(getBody(content, null, endFragmentId))
                }
                break
            }
        }
        elements.select("title").remove()
        elements.select("img").forEach {
            val src = it.attr("src")
            val path = chapter.href.substringBeforeLast("/", "")
            val absSrc = if (path.isEmpty()) src else StringUtil.collapsePathDots("$path/$src")
            it.attr("src", absSrc)
        }
        return HtmlFormatter.formatKeepImg(elements.outerHtml())
    }

    private fun getBody(
        content: Resource,
        startFragmentId: String?,
        endFragmentId: String?
    ): Element {
        var bodyElement = Jsoup.parse(String(content.data, charset)).body()
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

    private fun readEpub(uri: Uri): EpubBook {
        val parcelFileDescriptor = FileUtils.getParcelFileDescriptor(uri)!!
        fileDescriptor = parcelFileDescriptor
        val zipFile = AndroidZipFile(
            parcelFileDescriptor,
            fileName
        )
        return EpubReader().readEpubLazy(zipFile, "utf-8")
    }

    protected fun finalize() {
        fileDescriptor?.close()
    }
}