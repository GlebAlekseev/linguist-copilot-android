package pro.linguistcopilot.book

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.select.Elements
import org.junit.Test
import pro.linguistcopilot.core.book.util.HtmlFormatter


class FileProcessingTest {

    @Test
    fun testFileProcessing() {
        val document: Document = Jsoup.parse("<div id='example'>Hello, Jsoup!</div>")
        val elements: Elements = document.select("#example")
        val outerHtml = elements.outerHtml()
        println(HtmlFormatter.formatKeepImg(elements.outerHtml()))
    }
}