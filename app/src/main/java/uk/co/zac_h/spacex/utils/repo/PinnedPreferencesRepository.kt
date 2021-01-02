package uk.co.zac_h.spacex.utils.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PinnedPreferencesRepository(private val sharedPreferences: SharedPreferences) :
    PinnedPreferencesService {

    private val allPinned: MutableMap<String, Boolean>
        get() = sharedPreferences.all as MutableMap<String, Boolean>

    private val pinnedLiveData: MutableLiveData<MutableMap<String, Boolean>> = MutableLiveData()
    override val pinnedLive: LiveData<MutableMap<String, Boolean>> get() = pinnedLiveData

    private val preferencesChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            pinnedLiveData.value = allPinned
        }

    init {
        pinnedLiveData.value = allPinned

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferencesChangedListener)
    }

    override fun setPinnedLaunch(id: String, pinned: Boolean) {
        sharedPreferences.edit()?.putBoolean(id, pinned)?.apply()
    }

    override fun getAllPinnedLaunches(): MutableMap<String, *>? = sharedPreferences.all

    override fun isPinned(id: String): Boolean {
        return sharedPreferences.getBoolean(id, false)
    }

    override fun removePinnedLaunch(id: String) {
        sharedPreferences.edit()?.remove(id)?.apply()
    }
}