package pro.linguistcopilot.feature.translate.repository

import kotlinx.coroutines.flow.Flow
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig

interface TranslationEngineConfigRepository {
    val engineConfigs: List<TranslationEngineConfig>
    val engineConfigsAsFlow: Flow<List<TranslationEngineConfig>>
    fun updateEngineConfigs(engineConfigs: List<TranslationEngineConfig>)
}