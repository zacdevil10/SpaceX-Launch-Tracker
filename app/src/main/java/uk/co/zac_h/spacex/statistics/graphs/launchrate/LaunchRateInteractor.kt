package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.model.LaunchesModel

interface LaunchRateInteractor {

    fun getLaunches(listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?)
        fun onError(error: String)
    }
}