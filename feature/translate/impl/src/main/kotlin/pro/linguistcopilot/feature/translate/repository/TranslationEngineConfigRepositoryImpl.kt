package pro.linguistcopilot.feature.translate.repository

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import pro.linguistcopilot.feature.translate.entity.TranslationEngineConfig
import javax.inject.Inject

class TranslationEngineConfigRepositoryImpl @Inject constructor() :
    TranslationEngineConfigRepository {
    private val holder = MutableStateFlow(Json.encodeToString(initialTranslationEngineConfigs))
    private val _engineConfigs: MutableStateFlow<List<TranslationEngineConfig>> =
        MutableStateFlow(Json.decodeFromString<ListHolder>(holder.value).list)
    override val engineConfigs: List<TranslationEngineConfig>
        get() = _engineConfigs.value
    override val engineConfigsAsFlow: Flow<List<TranslationEngineConfig>>
        get() = _engineConfigs.onEach {
            holder.value = Json.encodeToString(it)
        }

    override fun updateEngineConfigs(engineConfigs: List<TranslationEngineConfig>) {
        _engineConfigs.value = engineConfigs
    }

    companion object {
        private val initialTranslationEngineConfigs = ListHolder(
            listOf(
                TranslationEngineConfig.Deepl("Deepl", "sk-ergt-ertjrh-rth", true),
                TranslationEngineConfig.MyMemory("MyMemory", "myMail@mail.com"),
                TranslationEngineConfig.MlKit("MlKit"),
            )
        )
    }
}

@Serializable
data class ListHolder(
    val list: List<TranslationEngineConfig>
)