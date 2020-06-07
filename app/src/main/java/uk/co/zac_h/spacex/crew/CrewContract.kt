package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.model.spacex.CrewModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CrewContract {

    interface CrewView {
        fun updateCrew(crew: List<CrewModel>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
    }

    interface CrewPresenter {
        fun getCrew(api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface CrewInteractor {
        fun getCrew(api: SpaceXInterface, listener: InteractorCallback)
        fun cancelAllRequests()
    }

    interface InteractorCallback {
        fun onSuccess(crew: List<CrewModel>?)
        fun onError(error: String)
    }

}