package pro.linguistcopilot.features.reader.presentation.view

import pro.linguistcopilot.features.reader.presentation.view.entities.TextPage

class TextPageFactory(
    private val readBook: ReadBook,
    private val readView: ReadView
) : PageFactory<TextPage>() {

    override fun hasPrev(): Boolean {
        return readBook.hasPrevChapter() || readBook.pageIndex > 0
    }

    override fun hasNext(): Boolean {
        return readBook.hasNextChapter() || readBook.currentChapter?.isLastIndex(readBook.pageIndex) != true
    }

    override fun hasNextPlus(): Boolean {
        return readBook.hasNextChapter() || readBook.pageIndex < (readBook.currentChapter?.pageSize
            ?: 1) - 2
    }

    override fun moveToFirst() {
        readBook.setPageIndex(0)
    }

    override fun moveToLast() {
        readBook.currentChapter?.let {
            if (it.pageSize == 0) {
                readBook.setPageIndex(0)
            } else {
                readBook.setPageIndex(it.pageSize.minus(1))
            }
        } ?: readBook.setPageIndex(0)
    }

    override fun moveToNext(upContent: Boolean): Boolean {
        return if (hasNext()) {
            if (readBook.currentChapter?.isLastIndex(readBook.pageIndex) == true) {
                readBook.moveToNextChapter(upContent)
            } else {
                readBook.setPageIndex(readBook.pageIndex.plus(1))
            }
            if (upContent) readView.refreshContent(0, false)
            true
        } else
            false
    }

    override fun moveToPrev(upContent: Boolean): Boolean {
        return if (hasPrev() && readBook.currentChapter != null) {
            if (readBook.pageIndex <= 0) {
                readBook.moveToPrevChapter(upContent)
            } else {
                readBook.setPageIndex(readBook.pageIndex.minus(1))
            }
            if (upContent) readView.refreshContent(0, false)
            true
        } else
            false
    }

    override val curPage: TextPage
        get() {
            readBook.currentChapter?.let {
                return it.getPage(readBook.pageIndex) ?: TextPage(
                    readBook = readBook,
                    title = it.title
                ).format()
            }
            return TextPage(readBook = readBook).format()
        }

    override val nextPage: TextPage
        get() {
            readBook.currentChapter?.let {
                if (readBook.pageIndex < it.pageSize - 1) {
                    return it.getPage(readBook.pageIndex + 1)?.removePageAloudSpan()
                        ?: TextPage(readBook = readBook, title = it.title).format()
                }
            }
            if (!readBook.hasNextChapter()) {
                return TextPage(readBook = readBook, text = "")
            }
            readBook.nextChapter?.let {
                return it.getPage(0)?.removePageAloudSpan()
                    ?: TextPage(readBook = readBook, title = it.title).format()
            }
            return TextPage(readBook = readBook).format()
        }

    override val prevPage: TextPage
        get() {
            if (readBook.pageIndex > 0) {
                readBook.currentChapter?.let {
                    return it.getPage(readBook.pageIndex - 1)?.removePageAloudSpan()
                        ?: TextPage(readBook = readBook, title = it.title).format()
                }
            }
            readBook.prevChapter?.let {
                return it.lastPage?.removePageAloudSpan()
                    ?: TextPage(readBook = readBook, title = it.title).format()
            }
            return TextPage(readBook = readBook).format()
        }

    override val nextPlusPage: TextPage
        get() {
            readBook.currentChapter?.let {
                if (readBook.pageIndex < it.pageSize - 2) {
                    return it.getPage(readBook.pageIndex + 2)?.removePageAloudSpan()
                        ?: TextPage(readBook = readBook, title = it.title).format()
                }
                readBook.nextChapter?.let { nc ->
                    if (readBook.pageIndex < it.pageSize - 1) {
                        return nc.getPage(0)?.removePageAloudSpan()
                            ?: TextPage(readBook = readBook, title = nc.title).format()
                    }
                    return nc.getPage(1)?.removePageAloudSpan()
                        ?: TextPage(readBook = readBook, text = "继续滑动以加载下一章…").format()
                }

            }
            return TextPage(readBook = readBook).format()
        }
}
