package uk.co.zac_h.spacex.launches.details.launch

interface LaunchDetailsHelper {

    fun pinLaunch(id: Int)

    fun isPinned(id: Int): Boolean

    fun removePinnedLaunch(id: Int)
}