package pro.linguistcopilot.features.reader.presentation.view

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import pro.linguistcopilot.features.reader.core.BookReader
import pro.linguistcopilot.features.reader.domain.Book
import pro.linguistcopilot.features.reader.domain.BookChapter
import pro.linguistcopilot.features.reader.presentation.view.coroutine.Coroutine
import pro.linguistcopilot.features.reader.presentation.view.entities.TextChapter
import pro.linguistcopilot.features.reader.presentation.view.entities.TextPage

class ReadBook(
    private val bookReader: BookReader,
) {
    val chapterProvider: ChapterProvider = ChapterProvider(
        bookReader = bookReader,
        textPageFactoryCallback = {
            return@ChapterProvider TextPage(readBook = this)
        }
    )


    val book: Book get() = bookReader.book


    var callBack: CallBack? = null
    var inBookshelf = false
    var tocChanged = false
    var chapterSize = 0
    var durChapterIndex = 0
    var durChapterPos = 0
    var isLocalBook = true
    var chapterChanged = false
    var prevChapter: TextChapter? = null
    var currentChapter: TextChapter? = null
    var nextChapter: TextChapter? = null
    var msg: String? = null
    private val loadingChapters = arrayListOf<Int>()
    var readStartTime: Long = System.currentTimeMillis()

    var preDownloadTask: Coroutine<*>? = null
    val downloadedChapters = hashSetOf<Int>()
    val downloadFailChapters = hashMapOf<Int, Int>()
    val downloadScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    init {
        val chapters = bookReader.getChapters()
        chapterSize = chapters.size
        contentLoadFinish(
            book = book,
            chapter = chapters[0],
            content = bookReader.getContent(chapters[0]).toString(),
            resetPageOffset = false
        )
    }

    fun moveToNextChapter(upContent: Boolean): Boolean {
        if (durChapterIndex < chapterSize - 1) {
            durChapterPos = 0
            durChapterIndex++
            prevChapter = currentChapter
            currentChapter = nextChapter
            nextChapter = null
            if (currentChapter == null) {
                callBack?.upContent()
                loadContent(durChapterIndex, upContent, resetPageOffset = false)
            } else if (upContent) {
                callBack?.upContent()
            }
            loadContent(durChapterIndex.plus(1), upContent, false)
            callBack?.upMenuView()
            return true
        } else {
            return false
        }
    }

    fun moveToPrevChapter(
        upContent: Boolean,
        toLast: Boolean = true
    ): Boolean {
        if (durChapterIndex > 0) {
            durChapterPos = if (toLast) prevChapter?.lastReadLength ?: Int.MAX_VALUE else 0
            durChapterIndex--
            nextChapter = currentChapter
            currentChapter = prevChapter
            prevChapter = null
            if (currentChapter == null) {
                callBack?.upContent()
                loadContent(durChapterIndex, upContent, resetPageOffset = false)
            } else if (upContent) {
                callBack?.upContent()
            }
            loadContent(durChapterIndex.minus(1), upContent, false)
            callBack?.upMenuView()
            return true
        } else {
            return false
        }
    }

    fun loadContent(
        index: Int,
        upContent: Boolean = true,
        resetPageOffset: Boolean = false,
        success: (() -> Unit)? = null
    ) {
        if (addLoading(index)) {
            Coroutine.async {
                bookReader.getChapters().find { it.index == index }?.let { chapter ->
                    bookReader.getContent(chapter)?.let {
                        contentLoadFinish(
                            book,
                            chapter,
                            it,
                            upContent,
                            resetPageOffset
                        ) {
                            success?.invoke()
                        }
                    }
                }
            }.onError {
                removeLoading(index)
            }
        }
    }

    private fun addLoading(index: Int): Boolean {
        synchronized(this) {
            if (loadingChapters.contains(index)) return false
            loadingChapters.add(index)
            return true
        }
    }

    fun removeLoading(index: Int) {
        synchronized(this) {
            loadingChapters.remove(index)
        }
    }


    fun setPageIndex(index: Int) {
        durChapterPos = currentChapter?.getReadLength(index) ?: index
    }

    val durPageIndex: Int
        get() {
            return currentChapter?.getPageIndexByCharIndex(durChapterPos) ?: durChapterPos
        }

    fun contentLoadFinish(
        book: Book,
        chapter: BookChapter,
        content: String,
        upContent: Boolean = true,
        resetPageOffset: Boolean,
        success: (() -> Unit)? = null
    ) {
        runCatching {
            removeLoading(chapter.index)
            if (chapter.index !in durChapterIndex - 1..durChapterIndex + 1) {
                return
            }
        }
        Coroutine.async {
            val displayTitle = chapter.title
            val textChapter = chapterProvider
                .getTextChapter(chapter, displayTitle, content, chapterSize)
            when (val offset = chapter.index - durChapterIndex) {
                0 -> {
                    currentChapter = textChapter
                    if (upContent) callBack?.upContent(offset, resetPageOffset)
                    callBack?.upMenuView()
                    callBack?.contentLoadFinish()
                }

                -1 -> {
                    prevChapter = textChapter
                    if (upContent) callBack?.upContent(offset, resetPageOffset)
                }

                1 -> {
                    nextChapter = textChapter
                    if (upContent) callBack?.upContent(offset, resetPageOffset)
                }
            }
            Unit
        }.onError {
        }.onSuccess {
            success?.invoke()
        }
    }


    fun hasNextChapter(): Boolean {
        return durChapterIndex < chapterSize - 1
    }

    fun hasPrevChapter(): Boolean {
        return durChapterIndex > 0
    }

    val pageIndex: Int get() = durPageIndex


    interface CallBack {
        fun upMenuView()

        fun loadChapterList(book: Book)

        fun upContent(
            relativePosition: Int = 0,
            resetPageOffset: Boolean = true,
            success: (() -> Unit)? = null
        )

        fun pageChanged()

        fun contentLoadFinish()

        fun upPageAnim()

        fun notifyBookChanged()
    }
}