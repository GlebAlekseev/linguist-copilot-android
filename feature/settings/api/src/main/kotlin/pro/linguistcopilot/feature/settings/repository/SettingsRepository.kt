package pro.linguistcopilot.feature.settings.repository

interface SettingsRepository {
    var isFreeDeeplApi : Boolean
    var freeDeeplApiKey : String?
    var proDeeplApiKey : String?
}