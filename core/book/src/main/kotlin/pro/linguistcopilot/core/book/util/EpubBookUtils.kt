package pro.linguistcopilot.core.book.util

import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import me.ag2s.epublib.domain.EpubBook
import java.io.File
import java.io.FileOutputStream

fun EpubBook.saveCompressedCover(uri: Uri) {
    if (!File(uri.path!!).exists()) {
        this.coverImage?.inputStream?.use { input ->
            val cover = BitmapFactory.decodeStream(input)
            val out = FileOutputStream(FileUtils.createFileIfNotExist(uri.path!!))
            cover.compress(Bitmap.CompressFormat.JPEG, 90, out)
            out.flush()
            out.close()
        }
    }
}