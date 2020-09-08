package uk.co.zac_h.spacex.utils.widgets.services

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchWidgetPresenter(
    private val service: LaunchWidgetContract.Service,
    private val interactor: LaunchWidgetContract.Interactor
) : LaunchWidgetContract.Presenter, LaunchWidgetContract.Callback {

    override fun getLaunch(id: Int, api: SpaceXInterface) {
        interactor.getLaunch(id, api, this)
    }

    override fun onSuccess(id: Int, launchModel: LaunchesExtendedModel?) {
        launchModel?.let { service.updateWidget(id, it) }
    }

    override fun onError(error: String) {
        service.showError(error)
    }

}