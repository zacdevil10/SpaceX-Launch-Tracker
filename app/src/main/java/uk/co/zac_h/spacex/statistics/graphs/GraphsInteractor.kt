package uk.co.zac_h.spacex.statistics.graphs

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface GraphsInteractor {

    fun getLaunches(id: String, listener: InteractorCallback)

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?)
        fun onError(error: String)
    }
}