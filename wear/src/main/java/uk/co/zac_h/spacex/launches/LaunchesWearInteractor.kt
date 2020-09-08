package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface LaunchesWearInteractor {

    fun getAllLaunches(id: String, order: String, listener: Callback)

    fun cancelRequest(): Unit?

    interface Callback {
        fun onSuccess(launches: List<LaunchesModel>?)
        fun onError(error: String)
    }
}