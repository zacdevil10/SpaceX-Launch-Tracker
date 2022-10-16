package uk.co.zac_h.spacex.base

import android.app.Application
import android.content.Context
import dagger.hilt.android.HiltAndroidApp
import uk.co.zac_h.spacex.network.DEFAULT_PREFERENCES
import uk.co.zac_h.spacex.utils.repo.ThemePreferenceRepository

@HiltAndroidApp
class App : Application() {

    lateinit var preferencesRepo: ThemePreferenceRepository

    override fun onCreate() {
        super.onCreate()

        preferencesRepo = ThemePreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
        )
    }
}
