package pro.linguistcopilot.feature.book.controller

import android.content.Context
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.core.file.openInputStreamByPath
import pro.linguistcopilot.feature.book.controller.LocalBookFileController.Companion.LOCAL_FILE_STORAGE_PATH
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream
import java.util.UUID
import javax.inject.Inject


class LocalBookFileControllerImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : LocalBookFileController {
    override fun copyToLocal(externalUri: String): String {
        val inputStream: FileInputStream = context.openInputStreamByPath(externalUri)
        val fileExtension = externalUri.takeLastWhile { it != '.' }
        val fileName = UUID.randomUUID()
        val localDirectory = File(context.filesDir, LOCAL_FILE_STORAGE_PATH)

        if (!localDirectory.exists()) {
            localDirectory.mkdirs()
        }
        val outputFile = File(localDirectory, "$fileName.$fileExtension")
        FileOutputStream(outputFile).use { outputStream ->
            inputStream.copyTo(outputStream)
        }
        inputStream.close()
        return outputFile.absolutePath
    }


}