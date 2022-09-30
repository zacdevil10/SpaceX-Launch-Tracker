package uk.co.zac_h.spacex.utils.repo

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import dagger.hilt.android.qualifiers.ApplicationContext
import uk.co.zac_h.spacex.PINNED_PREFERENCES
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinnedPreferencesRepository @Inject constructor(
    @ApplicationContext context: Context
) {

    private val preferences = context.getSharedPreferences(PINNED_PREFERENCES, Context.MODE_PRIVATE)

    private val allPinned: MutableMap<String, *>
        get() = preferences.all

    private val pinnedLiveData: MutableLiveData<MutableMap<String, *>> = MutableLiveData()
    val pinnedLive: LiveData<MutableMap<String, *>> get() = pinnedLiveData

    private val preferencesChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            pinnedLiveData.value = allPinned
        }

    init {
        pinnedLiveData.value = allPinned

        preferences.registerOnSharedPreferenceChangeListener(preferencesChangedListener)
    }

    fun setPinnedLaunch(id: String, pinned: Boolean) {
        preferences?.edit()?.putBoolean(id, pinned)?.apply()
    }

    fun getAllPinnedLaunches(): MutableMap<String, *>? = preferences?.all

    fun isPinned(id: String): Boolean {
        return preferences?.getBoolean(id, false) ?: false
    }

    fun removePinnedLaunch(id: String) {
        preferences?.edit()?.remove(id)?.apply()
    }
}
