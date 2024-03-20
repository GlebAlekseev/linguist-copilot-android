package pro.linguistcopilot.feature.settings.controller

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import pro.linguistcopilot.Constants
import pro.linguistcopilot.core.di.ApplicationContext
import pro.linguistcopilot.feature.settings.repository.SettingsRepository
import javax.inject.Inject

class SettingsRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : SettingsRepository {
    private val dataStore = context.dataStore
    private val isFreeDeeplApiKey = booleanPreferencesKey("isFreeDeeplApi")
    private val freeDeeplApiKeyKey = stringPreferencesKey("freeDeeplApiKey")
    private val proDeeplApiKeyKey = stringPreferencesKey("proDeeplApiKey")
    override var isFreeDeeplApi: Boolean
        get() {
            return runBlocking {
                dataStore.data.map { prefs -> prefs[isFreeDeeplApiKey] ?: true }.first()
            }
        }
        set(value) {
            runBlocking {
                dataStore.edit { prefs ->
                    prefs[isFreeDeeplApiKey] = value
                }
            }
        }
    override var freeDeeplApiKey: String?
        get() {
            return runBlocking {
                dataStore.data.map { prefs -> prefs[freeDeeplApiKeyKey].let { if (it == "") null else it } }
                    .first()
            }
        }
        set(value) {
            runBlocking {
                dataStore.edit { prefs ->
                    prefs[freeDeeplApiKeyKey] = value ?: ""
                }
            }
        }
    override var proDeeplApiKey: String?
        get() {
            return runBlocking {
                dataStore.data.map { prefs -> prefs[proDeeplApiKeyKey].let { if (it == "") null else it } }
                    .first()
            }
        }
        set(value) {
            runBlocking {
                dataStore.edit { prefs ->
                    prefs[proDeeplApiKeyKey] = value ?: ""
                }
            }
        }
}

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(Constants.PREFERENCES_STORAGE)
