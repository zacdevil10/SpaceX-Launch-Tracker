package uk.co.zac_h.spacex.launches.details.launch

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchDetailsInteractor {

    fun getSingleLaunch(id: String, listener: InteractorCallback)

    interface InteractorCallback {
        fun onSuccess(launchesModel: LaunchesModel?)
        fun onError(error: String)
    }
}