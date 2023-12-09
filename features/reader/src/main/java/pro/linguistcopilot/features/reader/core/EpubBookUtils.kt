package pro.linguistcopilot.features.reader.core

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import me.ag2s.epublib.domain.EpubBook
import java.io.File
import java.io.FileOutputStream

fun EpubBook.saveCompressedCover(url: String) {
    if (!File(url).exists()) {
        this.coverImage?.inputStream?.use { input ->
            val cover = BitmapFactory.decodeStream(input)
            val out = FileOutputStream(FileUtils.createFileIfNotExist(url))
            cover.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        }
    }
}