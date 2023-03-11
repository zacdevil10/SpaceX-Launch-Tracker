package uk.co.zac_h.spacex.base

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import uk.co.zac_h.spacex.feature.settings.theme.ThemePreferenceRepository
import javax.inject.Inject

@HiltAndroidApp
class App : Application() {

    @Inject
    lateinit var preferencesRepo: ThemePreferenceRepository

    override fun onCreate() {
        super.onCreate()

    }
}
