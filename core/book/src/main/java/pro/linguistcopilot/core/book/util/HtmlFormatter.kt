package pro.linguistcopilot.core.book.util

import java.util.regex.Pattern

@Suppress("RegExpRedundantEscape")
object HtmlFormatter {
    private val nbspRegex = "(&nbsp;)+".toRegex()
    private val espRegex = "(&ensp;|&emsp;)".toRegex()
    private val noPrintRegex = "(&thinsp;|&zwnj;|&zwj;)".toRegex()
    private val wrapHtmlRegex = "</?(?:div|p|br|hr|h\\d|article|dd|dl)[^>]*>".toRegex()
    private val commentRegex = "<!--[^>]*-->".toRegex() //注释
    private val notImgHtmlRegex = "</?(?!img)[a-zA-Z]+(?=[ >])[^<>]*>".toRegex()
    private val otherHtmlRegex = "</?[a-zA-Z]+(?=[ >])[^<>]*>".toRegex()
    private val formatImagePattern = Pattern.compile(
        "<img[^>]*\\ssrc\\s*=\\s*\"([^\"{>]*\\{(?:[^{}]|\\{[^}>]+\\})+\\})\"[^>]*>|<img[^>]*\\sdata-[^=>]*=\\s*\"([^\">]*)\"[^>]*>|<img[^>]*\\ssrc\\s*=\\s*\"([^\">]*)\"[^>]*>",
        Pattern.CASE_INSENSITIVE
    )

    fun format(html: String, otherRegex: Regex = otherHtmlRegex): String {
        return html.replace(nbspRegex, " ")
            .replace(espRegex, " ")
            .replace(noPrintRegex, "")
            .replace(wrapHtmlRegex, "\n")
            .replace(commentRegex, "")
            .replace(otherRegex, "")
            .replace("\\s*\\n+\\s*".toRegex(), "\n　　")
            .replace("^[\\n\\s]+".toRegex(), "　　")
            .replace("[\\n\\s]+$".toRegex(), "")
    }

    fun formatKeepImg(html: String): String {
        return format(html, notImgHtmlRegex)
    }
}
