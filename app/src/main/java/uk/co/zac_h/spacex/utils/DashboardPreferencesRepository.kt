package uk.co.zac_h.spacex.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_SECTION

object DashboardObj {
    var PREFERENCES_SECTION = ""

    const val PREFERENCES_NEXT_LAUNCH = "next_launch"
    const val PREFERENCES_LATEST_LAUNCH = "latest_launch"
    const val PREFERENCES_PINNED_LAUNCH = "pinned_launch"
    const val PREFERENCES_LATEST_NEWS = "latest_news"
}

class DashboardPreferencesRepository(private val sharedPreferences: SharedPreferences) {

    fun visible(section: String): MutableMap<String, *> = mutableMapOf(
        section to sharedPreferences.getBoolean(
            section,
            true
        )
    )

    private val allVisible: MutableMap<String, *>
        get() = sharedPreferences.all

    private val visibleLiveData: MutableLiveData<MutableMap<String, *>> = MutableLiveData()
    val visibleLive: LiveData<MutableMap<String, *>> get() = visibleLiveData

    var isVisible: MutableMap<String, *> = mutableMapOf(PREFERENCES_SECTION to true)
        get() = visible(PREFERENCES_SECTION)
        set(value) {
            sharedPreferences.edit().putBoolean(
                PREFERENCES_SECTION, (value[PREFERENCES_SECTION] ?: error("")) as Boolean
            ).apply()
            field = value
        }

    private val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            visibleLiveData.value = allVisible
        }

    init {
        visibleLiveData.value = allVisible

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

}