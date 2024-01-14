package uk.co.zac_h.spacex.feature.settings

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SettingsRepository @Inject constructor(
    private val dataStore: DataStore<Preferences>
) {

    private val NIGHT_MODE = intPreferencesKey("preferences_night_mode")
    val darkThemeConfigFlow: Flow<DarkThemeConfig> = dataStore.data.map { preferences ->
        val theme = preferences[NIGHT_MODE] ?: 0
        DarkThemeConfig.entries.first { it.value == theme }
    }

    private val DYNAMIC_COLOR = booleanPreferencesKey("preferences_dynamic_color")
    val useDynamicColorFlow: Flow<Boolean> = dataStore.data.map { preferences ->
        preferences[DYNAMIC_COLOR] ?: true
    }

    suspend fun setDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        dataStore.edit { preferences ->
            preferences[NIGHT_MODE] = darkThemeConfig.value
        }
    }

    suspend fun setDynamicColorPreferences(useDynamicColor: Boolean) {
        dataStore.edit { preferences ->
            preferences[DYNAMIC_COLOR] = useDynamicColor
        }
    }
}
