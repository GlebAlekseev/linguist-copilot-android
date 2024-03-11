package pro.linguistcopilot.core.file


import android.content.ContentResolver
import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileInputStream
import java.io.FileNotFoundException


fun Context.openInputStreamByPath(path: String): FileInputStream {
    return if (isContentUri(path)) {
        openInputStreamForContentUri(Uri.parse(path), contentResolver)
    } else {
        openInputStreamForFile(path)
    }
}

private fun isContentUri(path: String): Boolean {
    return path.startsWith("content://")
}

private fun openInputStreamForContentUri(
    uri: Uri,
    contentResolver: ContentResolver
): FileInputStream {
    return contentResolver.openInputStream(uri) as FileInputStream
}

private fun openInputStreamForFile(path: String): FileInputStream {
    return try {
        FileInputStream(File(path))
    } catch (e: FileNotFoundException) {
        throw RuntimeException("File not found: $path")
    }
}