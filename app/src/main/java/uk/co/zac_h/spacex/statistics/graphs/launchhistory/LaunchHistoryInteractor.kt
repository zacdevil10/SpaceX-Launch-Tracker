package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.model.LaunchesModel
import uk.co.zac_h.spacex.model.RocketsModel

interface LaunchHistoryInteractor {

    fun getLaunches(id: String, listener: InteractorCallback)

    fun getRockets(listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?, animate: Boolean)
        fun onRocketsSuccess(rockets: List<RocketsModel>?)
        fun onError(error: String)
    }
}