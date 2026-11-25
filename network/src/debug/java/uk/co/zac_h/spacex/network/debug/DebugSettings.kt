package uk.co.zac_h.spacex.network.debug

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStoreFile
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import uk.co.zac_h.spacex.core.database.SpaceXDatabase
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironment
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironmentSource
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Settings for the debug tools menu. Debug builds only: release builds have none of this.
 *
 * Kept in their own DataStore file ("debug_settings"), apart from the user's settings. To add a
 * toggle: add a key and a Flow/setter pair here, then a section in the app's DebugToolsDialog.
 */
@Singleton
class DebugSettings @Inject constructor(
    @ApplicationContext context: Context,
    private val database: SpaceXDatabase
) : LaunchLibraryEnvironmentSource {

    private val dataStore: DataStore<Preferences> = PreferenceDataStoreFactory.create(
        produceFile = { context.preferencesDataStoreFile("debug_settings") }
    )

    val launchLibraryEnvironment: Flow<LaunchLibraryEnvironment> = dataStore.data.map { preferences ->
        preferences[LAUNCH_LIBRARY_ENVIRONMENT]
            ?.let { name -> LaunchLibraryEnvironment.entries.firstOrNull { it.name == name } }
            ?: DEFAULT_ENVIRONMENT
    }

    // DataStore keeps the latest value in memory after the first read, so this is cheap.
    override fun current(): LaunchLibraryEnvironment = runBlocking { launchLibraryEnvironment.first() }

    /**
     * Switches the Launch Library server and deletes everything stored from the old one, so data
     * from the two servers never mixes. Restart the app afterwards to drop what is held in memory.
     */
    suspend fun setLaunchLibraryEnvironment(environment: LaunchLibraryEnvironment) {
        dataStore.edit { it[LAUNCH_LIBRARY_ENVIRONMENT] = environment.name }
        withContext(Dispatchers.IO) { database.clearAllTables() }
    }

    private companion object {
        val LAUNCH_LIBRARY_ENVIRONMENT = stringPreferencesKey("launch_library_environment")

        /** Debug builds have always used the development server, which is not rate limited. */
        val DEFAULT_ENVIRONMENT = LaunchLibraryEnvironment.DEVELOPMENT
    }
}
