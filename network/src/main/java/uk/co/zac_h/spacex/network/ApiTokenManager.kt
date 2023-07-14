package uk.co.zac_h.spacex.network

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ApiTokenManager @Inject constructor(
    @ApplicationContext private val context: Context
) {

    private val preferences: SharedPreferences =
        context.getSharedPreferences("subscription", Context.MODE_PRIVATE)

    var hasSubscribed: Boolean = false
        get() = preferences.getBoolean("subscribed", false)
        set(value) {
            field = value

            preferences.edit()?.putBoolean("subscribed", value)?.apply()
        }

    val token: String = BuildConfig.LAUNCH_LIBRARY_TOKEN
}