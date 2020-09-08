package uk.co.zac_h.spacex.utils

import android.content.SharedPreferences

class PinnedSharedPreferencesHelperImpl(private val preferences: SharedPreferences?) :
    PinnedSharedPreferencesHelper {

    override fun setPinnedLaunch(id: String, pinned: Boolean) {
        preferences?.edit()?.putBoolean(id, pinned)?.apply()
    }

    override fun getAllPinnedLaunches(): MutableMap<String, *>? = preferences?.all

    override fun isPinned(id: String): Boolean {
        return preferences?.getBoolean(id, false) ?: false
    }

    override fun removePinnedLaunch(id: String) {
        preferences?.edit()?.remove(id)?.apply()
    }
}