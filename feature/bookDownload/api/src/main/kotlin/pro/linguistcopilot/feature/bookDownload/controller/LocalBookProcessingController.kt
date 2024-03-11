package pro.linguistcopilot.feature.bookDownload.controller

import pro.linguistcopilot.feature.book.entity.MetaInfo

interface LocalBookProcessingController {
    fun getHash(uri: String): String
    fun isValidExt(uri: String): Boolean
    fun isValidSize(uri: String): Boolean
    fun isValidMimeType(uri: String): Boolean
    fun convert2epub(uri: String): String?
    fun convert2pdf(uri: String): String?
    fun getMetaInfo(pdfUri: String?, epubUri: String?): MetaInfo
    fun isSupportedExt(uri: String) = uri.endsWith(".epub") || uri.endsWith(".pdf")
}