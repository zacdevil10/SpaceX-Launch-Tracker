package uk.co.zac_h.spacex.utils.repo

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData

interface DashboardSharedPreferencesService {

    fun visible(section: String): MutableMap<String, *>

    val allVisible: MutableMap<String, *>

    val visibleLiveData: LiveData<MutableMap<String, *>>

    var isVisible: MutableMap<String, *>

    val preferenceChangedListener: SharedPreferences.OnSharedPreferenceChangeListener


}