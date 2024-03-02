package pro.linguistcopilot.book

import android.content.Context
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith
import pro.linguistcopilot.core.book.BookFactory
import pro.linguistcopilot.core.book.util.localParseUri
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream


@RunWith(AndroidJUnit4::class)
class YourAndroidTest {

    @Test
    fun testFileProcessing() {
        val context: Context = InstrumentationRegistry.getInstrumentation().targetContext
        val fileName = "test1.epub"
        val outputPath = context.filesDir.absolutePath + File.separator + fileName
        copyAssetToFile(context, fileName, outputPath)
        val result = processFile(outputPath)
        assert(result)
    }

    private fun processFile(filePath: String): Boolean {
        val uri = localParseUri(filePath)
        return try {
            val bookSource = BookFactory.getBookSource(uri)
            val metadata = bookSource.metadata
            println("metadata = $metadata")
            val chapters = bookSource.chapters
            println("chapters = $chapters")
            for (chapter in chapters) {
                println(chapter)
                val content = bookSource.getContent(chapter)
                println("content = \n$content")
            }
            true
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }

    private fun copyAssetToFile(context: Context, assetFileName: String, outputPath: String) {
        try {
            val inputStream: InputStream = context.assets.open(assetFileName)
            val outputStream = FileOutputStream(outputPath)
            val buffer = ByteArray(1024)
            var length: Int
            while ((inputStream.read(buffer).also { length = it }) > 0) {
                outputStream.write(buffer, 0, length)
            }
            inputStream.close()
            outputStream.close()
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
