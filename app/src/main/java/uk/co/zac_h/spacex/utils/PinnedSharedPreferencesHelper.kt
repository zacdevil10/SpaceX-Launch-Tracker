package uk.co.zac_h.spacex.utils

interface PinnedSharedPreferencesHelper {

    fun setPinnedLaunch(id: String, pinned: Boolean)

    fun getAllPinnedLaunches(): MutableMap<String, *>?

    fun isPinned(id: String): Boolean

    fun removePinnedLaunch(id: String)

}