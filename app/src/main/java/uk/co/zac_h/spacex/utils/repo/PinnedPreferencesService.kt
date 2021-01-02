package uk.co.zac_h.spacex.utils.repo

import androidx.lifecycle.LiveData

interface PinnedPreferencesService {

    val pinnedLive: LiveData<MutableMap<String, Boolean>>

    fun setPinnedLaunch(id: String, pinned: Boolean)

    fun getAllPinnedLaunches(): MutableMap<String, *>?

    fun isPinned(id: String): Boolean

    fun removePinnedLaunch(id: String)

}