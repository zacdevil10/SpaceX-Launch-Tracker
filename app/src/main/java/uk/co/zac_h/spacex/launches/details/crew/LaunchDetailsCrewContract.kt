package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.model.spacex.Crew
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsCrewContract {

    interface View {
        fun updateCrewRecyclerView(crewList: List<Crew>?)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface Presenter {
        fun getCrew(id: String, api: SpaceXInterface = SpaceXInterface.create())
        fun cancelRequest()
    }

    interface Interactor {
        fun getCrew(id: String, api: SpaceXInterface, listener: InteractorCallback)
        fun cancelRequest()
    }

    interface InteractorCallback {
        fun onSuccess(crew: List<Crew>?)
        fun onError(error: String)
    }
}