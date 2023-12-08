package io.legado.app.data.entities

import android.os.Parcelable
import androidx.annotation.IntDef
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pro.linguistcopilot.features.reader.core.book.MD5Utils
import pro.linguistcopilot.features.reader.domain.BaseBook
import pro.linguistcopilot.features.reader.domain.GSON
import pro.linguistcopilot.features.reader.domain.fromJsonObject
import java.nio.charset.Charset
import kotlin.math.max
import kotlin.math.min

object BookType {
    /**
     * 8 文本
     */
    const val text = 0b1000

    /**
     * 16 更新失败
     */
    const val updateError = 0b10000

    /**
     * 32 音频
     */
    const val audio = 0b100000

    /**
     * 64 图片
     */
    const val image = 0b1000000

    /**
     * 128 只提供下载服务的网站
     */
    const val webFile = 0b10000000

    /**
     * 256 本地
     */
    const val local = 0b100000000

    /**
     * 512 压缩包 表明书籍文件是从压缩包内解压来的
     */
    const val archive = 0b1000000000

    @Target(AnnotationTarget.VALUE_PARAMETER)
    @Retention(AnnotationRetention.SOURCE)
    @IntDef(text, updateError, audio, image, webFile, local, archive)
    annotation class Type


    /**
     * 本地书籍书源标志
     */
    const val localTag = "loc_book"

    /**
     * 书源已webDav::开头的书籍,可以从webDav更新或重新下载
     */
    const val webDavTag = "webDav::"

}

val authorRegex = Regex("^\\s*作\\s*者[:：\\s]+|\\s+著")

@Parcelize
data class Book(
    // 详情页Url(本地书源存储完整文件路径)
    override var bookUrl: String = "",
    // 目录页Url (toc=table of Contents)
    var tocUrl: String = "",
    // 书源URL(默认BookType.local)
    var origin: String = BookType.localTag,
    //书源名称 or 本地书籍文件名
    var originName: String = "",
    // 书籍名称(书源获取)
    override var name: String = "",
    // 作者名称(书源获取)
    override var author: String = "",
    // 分类信息(书源获取)
    override var kind: String? = null,
    // 分类信息(用户修改)
    var customTag: String? = null,
    // 封面Url(书源获取)
    var coverUrl: String? = null,
    // 封面Url(用户修改)
    var customCoverUrl: String? = null,
    // 简介内容(书源获取)
    var intro: String? = null,
    // 简介内容(用户修改)
    var customIntro: String? = null,
    // 自定义字符集名称(仅适用于本地书籍)
    var charset: String? = null,
    // 类型,详见BookType
    var type: Int = BookType.text,
    // 自定义分组索引号
    var group: Long = 0,
    // 最新章节标题
    var latestChapterTitle: String? = null,
    // 最新章节标题更新时间
    var latestChapterTime: Long = System.currentTimeMillis(),
    // 最近一次更新书籍信息的时间
    var lastCheckTime: Long = System.currentTimeMillis(),
    // 最近一次发现新章节的数量
    var lastCheckCount: Int = 0,
    // 书籍目录总数
    var totalChapterNum: Int = 0,
    // 当前章节名称
    var durChapterTitle: String? = null,
    // 当前章节索引
    var durChapterIndex: Int = 0,
    // 当前阅读的进度(首行字符的索引位置)
    var durChapterPos: Int = 0,
    // 最近一次阅读书籍的时间(打开正文的时间)
    var durChapterTime: Long = System.currentTimeMillis(),
    //字数
    override var wordCount: String? = null,
    // 刷新书架时更新书籍信息
    var canUpdate: Boolean = true,
    // 手动排序
    var order: Int = 0,
    //书源排序
    var originOrder: Int = 0,
    // 自定义书籍变量信息(用于书源规则检索书籍信息)
    override var variable: String? = null,
    //阅读设置
    var readConfig: ReadConfig? = null,
    //同步时间
    var syncTime: Long = 0L
) : Parcelable, BaseBook {

    override fun equals(other: Any?): Boolean {
        if (other is Book) {
            return other.bookUrl == bookUrl
        }
        return false
    }

    override fun hashCode(): Int {
        return bookUrl.hashCode()
    }

    override val variableMap: HashMap<String, String> by lazy {
        GSON.fromJsonObject<HashMap<String, String>>(variable).getOrNull() ?: hashMapOf()
    }

    override var infoHtml: String? = null

    override var tocHtml: String? = null

    var downloadUrls: List<String>? = null

    private var folderName: String? = null

    fun getRealAuthor() = author.replace(authorRegex, "")

    fun getUnreadChapterNum() = max(totalChapterNum - durChapterIndex - 1, 0)

    fun getDisplayCover() = if (customCoverUrl.isNullOrEmpty()) coverUrl else customCoverUrl

    fun getDisplayIntro() = if (customIntro.isNullOrEmpty()) intro else customIntro

    //自定义简介有自动更新的需求时，可通过更新intro再调用upCustomIntro()完成
    @Suppress("unused")
    fun upCustomIntro() {
        customIntro = intro
    }

    fun fileCharset(): Charset {
        return charset(charset ?: "UTF-8")
    }

    @IgnoredOnParcel
    val config: ReadConfig
        get() {
            if (readConfig == null) {
                readConfig = ReadConfig()
            }
            return readConfig!!
        }

    fun setReverseToc(reverseToc: Boolean) {
        config.reverseToc = reverseToc
    }

    fun getReverseToc(): Boolean {
        return config.reverseToc
    }

    fun setUseReplaceRule(useReplaceRule: Boolean) {
        config.useReplaceRule = useReplaceRule
    }

    fun getUseReplaceRule(): Boolean {
        val useReplaceRule = config.useReplaceRule
        if (useReplaceRule != null) {
            return useReplaceRule
        }
        //图片类书源 epub本地 默认关闭净化
        return false
    }

    fun setReSegment(reSegment: Boolean) {
        config.reSegment = reSegment
    }

    fun getReSegment(): Boolean {
        return config.reSegment
    }

    fun setPageAnim(pageAnim: Int?) {
        config.pageAnim = pageAnim
    }


    fun getPageAnim(): Int {
        var pageAnim = config.pageAnim
            ?: if (type and BookType.image > 0) 3 else 3
        if (pageAnim < 0) {
            pageAnim = 3
        }
        return pageAnim
    }

    fun setImageStyle(imageStyle: String?) {
        config.imageStyle = imageStyle
    }

    fun getImageStyle(): String? {
        return config.imageStyle
            ?: if (false) imgStyleFull else null
    }

    fun setTtsEngine(ttsEngine: String?) {
        config.ttsEngine = ttsEngine
    }

    fun getTtsEngine(): String? {
        return config.ttsEngine
    }

    fun setSplitLongChapter(limitLongContent: Boolean) {
        config.splitLongChapter = limitLongContent
    }

    fun getSplitLongChapter(): Boolean {
        return config.splitLongChapter
    }

    fun getDelTag(tag: Long): Boolean {
        return config.delTag and tag == tag
    }

    fun getFolderName(): String {
        folderName?.let {
            return it
        }
        //防止书名过长,只取9位
        folderName = getFolderNameNoCache()
        return folderName!!
    }

    fun getFolderNameNoCache(): String {
        return name.replace(Regex("[\\\\/:*?\"<>|.]"), "").let {
            it.substring(0, min(9, it.length)) + MD5Utils.md5Encode16(bookUrl)
        }
    }


    /**
     * 迁移旧的书籍的一些信息到新的书籍中
     */
//    fun migrateTo(newBook: Book, toc: List<BookChapter>): Book {
//        newBook.durChapterIndex = BookHelp
//            .getDurChapter(durChapterIndex, durChapterTitle, toc, totalChapterNum)
//        newBook.durChapterTitle = toc[newBook.durChapterIndex].getDisplayTitle(
//            ContentProcessor.get(newBook.name, newBook.origin).getTitleReplaceRules()
//        )
//        newBook.durChapterPos = durChapterPos
//        newBook.durChapterTime = durChapterTime
//        newBook.group = group
//        newBook.order = order
//        newBook.customCoverUrl = customCoverUrl
//        newBook.customIntro = customIntro
//        newBook.customTag = customTag
//        newBook.canUpdate = canUpdate
//        newBook.readConfig = readConfig
//        return newBook
//    }

//    fun createBookMark(): Bookmark {
//        return Bookmark(
//            bookName = name,
//            bookAuthor = author,
//        )
//    }
//
//    fun save() {
//        if (appDb.bookDao.has(bookUrl) == true) {
//            appDb.bookDao.update(this)
//        } else {
//            appDb.bookDao.insert(this)
//        }
//    }
//
//    fun delete() {
//        if (ReadBook.book?.bookUrl == bookUrl) {
//            ReadBook.book = null
//        }
//        appDb.bookDao.delete(this)
//    }

    companion object {
        const val hTag = 2L
        const val rubyTag = 4L
        const val imgStyleDefault = "DEFAULT"
        const val imgStyleFull = "FULL"
        const val imgStyleText = "TEXT"
    }

    @Parcelize
    data class ReadConfig(
        var reverseToc: Boolean = false,
        var pageAnim: Int? = null,
        var reSegment: Boolean = false,
        var imageStyle: String? = null,
        var useReplaceRule: Boolean? = null,// 正文使用净化替换规则
        var delTag: Long = 0L,//去除标签
        var ttsEngine: String? = null,
        var splitLongChapter: Boolean = true
    ) : Parcelable

//    class Converters {
//
//        @TypeConverter
//        fun readConfigToString(config: ReadConfig?): String = GSON.toJson(config)
//
//        @TypeConverter
//        fun stringToReadConfig(json: String?) = GSON.fromJsonObject<ReadConfig>(json).getOrNull()
//    }
}