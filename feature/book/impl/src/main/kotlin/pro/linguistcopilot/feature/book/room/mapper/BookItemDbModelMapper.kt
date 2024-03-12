package pro.linguistcopilot.feature.book.room.mapper

import pro.linguistcopilot.feature.book.entity.BookItem
import pro.linguistcopilot.feature.book.room.model.BookItemDbModel

fun BookItem.mapToDb(): BookItemDbModel {
    return BookItemDbModel(
        id = this.id,
        title = this.title,
        uri = this.uri,
        createdAt = this.createdAt,
        isBad = this.isBad,
        isValid = this.isValid,
        errorMessage = this.errorMessage,
        epubUri = this.epubUri ?: "",
        pdfUri = this.pdfUri ?: "",
        metaInfo = this.metaInfo,
        hash = this.hash,
    )
}

fun BookItemDbModel.mapToDomain(): BookItem {
    return BookItem(
        id = this.id,
        title = this.title,
        uri = this.uri,
        createdAt = this.createdAt,
        isBad = this.isBad,
        isValid = this.isValid,
        errorMessage = this.errorMessage,
        epubUri = this.epubUri.let { if (it == "") null else it },
        pdfUri = this.pdfUri.let { if (it == "") null else it },
        metaInfo = this.metaInfo,
        hash = this.hash,
    )
}