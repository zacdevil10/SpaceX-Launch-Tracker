package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.LaunchesModel

interface LaunchDetailsInteractor {

    fun getSingleLaunch(id: String, listener: InteractorCallback)

    interface InteractorCallback {
        fun onSuccess(launchModel: LaunchesModel?)
        fun onError(error: String)
    }
}