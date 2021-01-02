package uk.co.zac_h.spacex.utils.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

class PinnedPreferencesRepository(private val sharedPreferences: SharedPreferences) {

    private val allPinned: MutableMap<String, *>
        get() = sharedPreferences.all

    private val pinnedLiveData: MutableLiveData<MutableMap<String, *>> = MutableLiveData()
    val pinnedLive: LiveData<MutableMap<String, *>> get() = pinnedLiveData

    private val preferencesChangedListener =
        SharedPreferences.OnSharedPreferenceChangeListener { _, _ ->
            pinnedLiveData.value = allPinned
        }

    init {
        pinnedLiveData.value = allPinned

        sharedPreferences.registerOnSharedPreferenceChangeListener(preferencesChangedListener)
    }
}