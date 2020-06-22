package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsContract {

    interface LaunchDetailsView {
        fun updateLaunchDataView(launch: LaunchesExtendedModel?)
        fun newCalendarEvent()
        fun openWebLink(link: String)
        fun showProgress()
        fun hideProgress()
        fun updateCountdown(countdown: String)
        fun setCountdown(time: Long)
        fun showCountdown()
        fun hideCountdown()
        fun showError(error: String)
    }

    interface LaunchDetailsPresenter {
        fun getLaunch(flightNumber: Int, api: SpaceXInterface = SpaceXInterface.create())
        fun addLaunchModel(launchModel: LaunchesExtendedModel?)
        fun pinLaunch(id: String, pin: Boolean)
        fun isPinned(id: String): Boolean
        fun createEvent()
        fun updateCountdown(time: Long)
        fun cancelRequest()
    }

    interface LaunchDetailsInteractor {
        fun getSingleLaunch(flightNumber: Int, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest(): Unit?
    }

    interface InteractorCallback {
        fun onSuccess(launchModel: LaunchesExtendedDocsModel?)
        fun onError(error: String)
    }
}