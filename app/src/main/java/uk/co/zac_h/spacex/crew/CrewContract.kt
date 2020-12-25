package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CrewContract {

    interface CrewView {
        fun updateCrew(crew: List<Crew>)
        fun showProgress()
        fun hideProgress()
        fun toggleSwipeRefresh(refreshing: Boolean)
        fun showError(error: String)
        fun startTransition()
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
        fun onSuccess(crew: List<Crew>?)
        fun onError(error: String)
    }

}