package pro.linguistcopilot.feature.book.controller

interface LocalBookFileController {
    fun copyToLocal(externalUri: String): String
    companion object {
        const val LOCAL_FILE_STORAGE_PATH = "local_files"
    }
}