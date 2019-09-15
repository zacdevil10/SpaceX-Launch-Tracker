package uk.co.zac_h.spacex.utils

import android.content.SharedPreferences

class PinnedSharedPreferencesHelper(private val preferences: SharedPreferences?) {

    fun addPinnedLaunch(id: String) {
        preferences?.edit()?.putBoolean(id, true)?.apply()
    }

    fun isPinned(id: String): Boolean {
        return preferences?.getBoolean(id, false) ?: false
    }

    fun removePinnedLaunch(id: String) {
        preferences?.edit()?.remove(id)?.apply()
    }
}