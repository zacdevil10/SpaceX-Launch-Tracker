package uk.co.zac_h.spacex.statistics.graphs.padstats

import uk.co.zac_h.spacex.utils.data.LaunchpadModel

interface PadStatsInteractor {

    fun getLaunchpads(listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(launchpads: List<LaunchpadModel>?)
        fun onError(error: String)
    }

}