package uk.co.zac_h.spacex.utils

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class DashboardPreferencesRepository(private val sharedPreferences: SharedPreferences) {

    var PREFERENCES_SECTION = ""

    val PREFERENCES_NEXT_LAUNCH = "next_launch"
    val PREFERENCES_LATEST_LAUNCH = "latest_launch"
    val PREFERENCES_PINNED_LAUNCH = "pinned_launch"
    val PREFERENCES_LATEST_NEWS = "latest_news"

    fun visible(section: String): MutableMap<String, *> = mutableMapOf(
        section to sharedPreferences.getBoolean(
            section,
            true
        )
    )

    val allVisible: MutableMap<String, *>
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
        SharedPreferences.OnSharedPreferenceChangeListener { _, key ->
            when (key) {
                PREFERENCES_NEXT_LAUNCH -> {
                    PREFERENCES_SECTION = PREFERENCES_NEXT_LAUNCH
                    visibleLiveData.value = visible(PREFERENCES_SECTION)
                }
                PREFERENCES_LATEST_LAUNCH -> {
                    PREFERENCES_SECTION = PREFERENCES_LATEST_LAUNCH
                    visibleLiveData.value = visible(PREFERENCES_SECTION)
                }
                PREFERENCES_PINNED_LAUNCH -> {
                    PREFERENCES_SECTION = PREFERENCES_PINNED_LAUNCH
                    visibleLiveData.value = visible(PREFERENCES_SECTION)
                }
                PREFERENCES_LATEST_NEWS -> {
                    PREFERENCES_SECTION = PREFERENCES_LATEST_NEWS
                    visibleLiveData.value = visible(PREFERENCES_SECTION)
                }
            }
        }

    init {
        visibleLiveData.value = allVisible

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

}