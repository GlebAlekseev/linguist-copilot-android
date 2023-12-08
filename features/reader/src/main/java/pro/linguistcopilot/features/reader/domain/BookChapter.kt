package io.legado.app.data.entities

import android.os.Parcelable
import kotlinx.coroutines.CancellationException
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import pro.linguistcopilot.features.reader.core.book.MD5Utils
import pro.linguistcopilot.features.reader.core.book.NetworkUtils
import pro.linguistcopilot.features.reader.core.book.RuleBigDataHelp
import pro.linguistcopilot.features.reader.core.book.RuleDataInterface
import pro.linguistcopilot.features.reader.domain.GSON
import pro.linguistcopilot.features.reader.domain.fromJsonObject
import splitties.init.appCtx
import java.util.regex.Pattern


@Parcelize
data class BookChapter(
    var url: String = "",               // 章节地址
    var title: String = "",             // 章节标题
    var isVolume: Boolean = false,      // 是否是卷名
    var baseUrl: String = "",           // 用来拼接相对url
    var bookUrl: String = "",           // 书籍地址
    var index: Int = 0,                 // 章节序号
    var isVip: Boolean = false,         // 是否VIP
    var isPay: Boolean = false,         // 是否已购买
    var resourceUrl: String? = null,    // 音频真实URL
    var tag: String? = null,            // 更新时间或其他章节附加信息
    var start: Long? = null,            // 章节起始位置
    var end: Long? = null,              // 章节终止位置
    var startFragmentId: String? = null,  //EPUB书籍当前章节的fragmentId
    var endFragmentId: String? = null,    //EPUB书籍下一章节的fragmentId
    var variable: String? = null        //变量
) : Parcelable, RuleDataInterface {

    @IgnoredOnParcel
    override val variableMap: HashMap<String, String> by lazy {
        GSON.fromJsonObject<HashMap<String, String>>(variable).getOrNull() ?: hashMapOf()
    }

    @IgnoredOnParcel
    private val titleMD5: String by lazy {
        MD5Utils.md5Encode16(title)
    }

    override fun putVariable(key: String, value: String?): Boolean {
        if (super.putVariable(key, value)) {
            variable = GSON.toJson(variableMap)
        }
        return true
    }

    override fun putBigVariable(key: String, value: String?) {
        RuleBigDataHelp.putChapterVariable(bookUrl, url, key, value)
    }

    override fun getBigVariable(key: String): String? {
        return RuleBigDataHelp.getChapterVariable(bookUrl, url, key)
    }

    override fun hashCode() = url.hashCode()

    override fun equals(other: Any?): Boolean {
        if (other is BookChapter) {
            return other.url == url
        }
        return false
    }

    fun primaryStr(): String {
        return bookUrl + url
    }

    fun getDisplayTitle(
        useReplace: Boolean = true,
        chineseConvert: Boolean = true,
    ): String {
        var displayTitle = "title"
        return when {
            !isVip -> displayTitle
            isPay -> "R.string.payed_title"
            else -> "R.string.vip_title"
        }
    }

    fun getAbsoluteURL(): String {
        //二级目录解析的卷链接为空 返回目录页的链接
        if (url.startsWith(title) && isVolume) return baseUrl
        val urlMatcher = Pattern.compile("\\s*,\\s*(?=\\{)").matcher(url)
        val urlBefore = if (urlMatcher.find()) url.substring(0, urlMatcher.start()) else url
        val urlAbsoluteBefore = NetworkUtils.getAbsoluteURL(baseUrl, urlBefore)
        return if (urlBefore.length == url.length) {
            urlAbsoluteBefore
        } else {
            "$urlAbsoluteBefore," + url.substring(urlMatcher.end())
        }
    }

    @Suppress("unused")
    fun getFileName(suffix: String = "nb"): String =
        String.format("%05d-%s.%s", index, titleMD5, suffix)


    @Suppress("unused")
    fun getFontName(): String = String.format("%05d-%s.ttf", index, titleMD5)
}

