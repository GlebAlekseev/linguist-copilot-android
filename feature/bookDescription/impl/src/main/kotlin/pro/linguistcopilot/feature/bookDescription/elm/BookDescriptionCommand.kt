package pro.linguistcopilot.feature.bookDescription.elm


sealed class BookDescriptionCommand {
    data class GetBookItem(val bookId: String) : BookDescriptionCommand()
}