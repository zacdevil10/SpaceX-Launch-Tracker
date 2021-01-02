package uk.co.zac_h.spacex.utils.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import uk.co.zac_h.spacex.utils.repo.DashboardObject.PREFERENCES_SECTION
import javax.inject.Inject
import javax.inject.Singleton

object DashboardObject {
    var PREFERENCES_SECTION = ""

    const val PREFERENCES_NEXT_LAUNCH = "next_launch"
    const val PREFERENCES_PREVIOUS_LAUNCH = "previous_launch"
    const val PREFERENCES_PINNED_LAUNCH = "pinned_launch"
    const val PREFERENCES_LATEST_NEWS = "latest_news"
}

class DashboardPreferencesRepository constructor(
    private val sharedPreferences: SharedPreferences
) : DashboardSharedPreferencesService {

    override fun visible(section: String): MutableMap<String, *> = mutableMapOf(
        section to sharedPreferences.getBoolean(section, true)
    )

    override val allVisible: MutableMap<String, *>
        get() = sharedPreferences.all

    private val _visibleLiveData: MutableLiveData<MutableMap<String, *>> = MutableLiveData()
    override val visibleLiveData: LiveData<MutableMap<String, *>> get() = _visibleLiveData

    override var isVisible: MutableMap<String, *> = mutableMapOf(PREFERENCES_SECTION to true)
        get() = visible(PREFERENCES_SECTION)
        set(value) {
            sharedPreferences.edit().putBoolean(
                PREFERENCES_SECTION, (value[PREFERENCES_SECTION] ?: error("")) as Boolean
            ).apply()
            field = value
        }

    override val preferenceChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            _visibleLiveData.value = allVisible
        }

    init {
        _visibleLiveData.value = allVisible

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferenceChangedListener)
    }

}