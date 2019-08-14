package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchesInteractor {

    fun getLaunches(id: String, listener: InteractorCallback)

    fun cancelAllRequests()

    interface InteractorCallback {
        fun onSuccess(launches: List<LaunchesModel>?)
        fun onError(error: String)
    }

}