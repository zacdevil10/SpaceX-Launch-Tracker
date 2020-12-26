package uk.co.zac_h.spacex.launches.details.payloads

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsPayloadsPresenter(
    private val view: LaunchDetailsPayloadsContract.View,
    private val interactor: LaunchDetailsPayloadsContract.Interactor
) : LaunchDetailsPayloadsContract.Presenter, LaunchDetailsPayloadsContract.InteractorCallback {

    override fun getLaunch(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getPayloads(id, api, this)
    }

    override fun cancelRequest() {
        interactor.cancelRequest()
    }

    override fun onSuccess(launch: Launch?) {
        launch?.payloads?.let { payloads ->
            view.updatePayloadsRecyclerView(payloads)
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}