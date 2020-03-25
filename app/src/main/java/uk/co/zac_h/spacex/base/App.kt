package uk.co.zac_h.spacex.base

import android.app.Application
import android.content.Context
import uk.co.zac_h.spacex.utils.DashboardPreferencesRepository
import uk.co.zac_h.spacex.utils.PinnedPreferencesRepository
import uk.co.zac_h.spacex.utils.ThemePreferenceRepository
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class App : Application() {

    lateinit var preferencesRepo: ThemePreferenceRepository
    lateinit var dashboardPreferencesRepo: DashboardPreferencesRepository
    lateinit var pinnedPreferencesRepo: PinnedPreferencesRepository
    lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    override fun onCreate() {
        super.onCreate()

        //TooLargeTool.startLogging(this)

        preferencesRepo = ThemePreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
        )
        dashboardPreferencesRepo = DashboardPreferencesRepository(
            getSharedPreferences(DASHBOARD_PREFERENCES, Context.MODE_PRIVATE)
        )
        pinnedPreferencesRepo = PinnedPreferencesRepository(
            getSharedPreferences(PINNED_PREFERENCES, Context.MODE_PRIVATE)
        )
        networkStateChangeListener = OnNetworkStateChangeListener(this)
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"
        const val DASHBOARD_PREFERENCES = "dashboard_preferences"
        const val PINNED_PREFERENCES = "pinned"
    }
}