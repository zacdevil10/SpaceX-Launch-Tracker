package uk.co.zac_h.spacex.utils.widgets.services

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchWidgetContract {

    interface Service {
        fun updateWidget(id: Int, launchModel: LaunchesExtendedModel)
        fun showError(error: String)
    }

    interface Presenter {
        fun getLaunch(id: Int, api: SpaceXInterface = SpaceXInterface.create())
    }

    interface Interactor {
        fun getLaunch(id: Int, api: SpaceXInterface, listener: Callback)
    }

    interface Callback {
        fun onSuccess(id: Int, launchModel: LaunchesExtendedModel?)
        fun onError(error: String)
    }

}