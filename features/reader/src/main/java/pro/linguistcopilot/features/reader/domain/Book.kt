package pro.linguistcopilot.features.reader.domain


data class Book(
    var url: String,
    var coverUrl: String,
    var name: String,
    var intro: String?,
    var author: String?
)