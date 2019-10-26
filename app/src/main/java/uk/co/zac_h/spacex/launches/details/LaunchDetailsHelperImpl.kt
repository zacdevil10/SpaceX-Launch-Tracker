package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class LaunchDetailsHelperImpl(private val pinnedSharedPreferences: PinnedSharedPreferencesHelper) :
    LaunchDetailsHelper {

    override fun pinLaunch(id: Int) {
        pinnedSharedPreferences.addPinnedLaunch(id.toString())
    }

    override fun isPinned(id: Int): Boolean = pinnedSharedPreferences.isPinned(id.toString())

    override fun removePinnedLaunch(id: Int) {
        pinnedSharedPreferences.removePinnedLaunch(id.toString())
    }
}