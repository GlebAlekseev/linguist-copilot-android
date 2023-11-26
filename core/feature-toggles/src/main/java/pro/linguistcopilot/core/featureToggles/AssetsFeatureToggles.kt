package pro.linguistcopilot.core.featureToggles

import android.content.Context
import com.google.gson.Gson
import com.google.gson.JsonSyntaxException
import pro.linguistcopilot.core.utils.readJsonFromAssets

class AssetsFeatureToggles constructor(
    private val context: Context,
    private val default: IFeatureToggles? = null
) : IFeatureToggles {
    private var _featureToggles: FeatureToggles? = null
    override val featureToggles: FeatureToggles
        get() = _featureToggles ?: throw RuntimeException("Can't find the configuration")

    override fun load() {
        if (default != null) {
            default.load()
            _featureToggles = default.featureToggles
        }
        val jsonString = readJsonFromAssets(context, "feature-toggles.json").toString()
        _featureToggles = try {
            Gson().fromJson(jsonString, FeatureToggles::class.java)
        } catch (e: JsonSyntaxException) {
            null
        }
    }
}