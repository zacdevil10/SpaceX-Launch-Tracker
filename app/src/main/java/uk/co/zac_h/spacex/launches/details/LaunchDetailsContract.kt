package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsContract {

    interface LaunchDetailsView {
        fun updateLaunchDataView(launch: LaunchesModel?)
        fun newCalendarEvent()
        fun openWebLink(link: String)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface LaunchDetailsPresenter {
        fun getLaunch(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchModel(launchModel: LaunchesModel?)
        fun pinLaunch(id: String, pin: Boolean)
        fun isPinned(id: String): Boolean
        fun createEvent()
        fun cancelRequest()
    }

    interface LaunchDetailsInteractor {
        fun getSingleLaunch(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(launchModel: LaunchesModel?)
        fun onError(error: String)
    }
}