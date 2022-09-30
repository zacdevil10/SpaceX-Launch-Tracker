package uk.co.zac_h.spacex.utils

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PinnedSharedPreferences @Inject constructor(
    @ApplicationContext context: Context
) {

    private val preferences = context.getSharedPreferences("pinned", Context.MODE_PRIVATE)

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
