package uk.co.zac_h.spacex.utils

interface PinnedSharedPreferencesHelper {

    fun addPinnedLaunch(id: String)

    fun getAllPinnedLaunches(): MutableMap<String, *>?

    fun isPinned(id: String): Boolean

    fun removePinnedLaunch(id: String)

}