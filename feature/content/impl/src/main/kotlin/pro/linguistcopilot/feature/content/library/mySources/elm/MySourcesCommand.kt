package pro.linguistcopilot.feature.content.library.mySources.elm


sealed class MySourcesCommand {
    data object GetList : MySourcesCommand()
}