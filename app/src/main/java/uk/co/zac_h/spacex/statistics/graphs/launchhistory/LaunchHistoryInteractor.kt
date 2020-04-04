package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface LaunchHistoryInteractor {

    fun getLaunches(listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean)
        fun onError(error: String)
    }
}