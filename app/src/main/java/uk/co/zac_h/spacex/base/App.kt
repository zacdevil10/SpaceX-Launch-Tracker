package uk.co.zac_h.spacex.base

import android.app.Application
import android.content.Context
import uk.co.zac_h.spacex.utils.PreferenceRepository
import uk.co.zac_h.spacex.utils.network.OnNetworkStateChangeListener

class App : Application() {

    lateinit var preferencesRepo: PreferenceRepository
    lateinit var networkStateChangeListener: OnNetworkStateChangeListener

    override fun onCreate() {
        super.onCreate()
        preferencesRepo = PreferenceRepository(
            getSharedPreferences(DEFAULT_PREFERENCES, Context.MODE_PRIVATE)
        )
        networkStateChangeListener = OnNetworkStateChangeListener(this)
    }

    companion object {
        const val DEFAULT_PREFERENCES = "default_preferences"
    }
}