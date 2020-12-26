package uk.co.zac_h.spacex.launches.details.ships

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface

class LaunchDetailsShipsPresenter(
    private val view: LaunchDetailsShipsContract.View,
    private val interactor: LaunchDetailsShipsContract.Interactor
) : LaunchDetailsShipsContract.Presenter, LaunchDetailsShipsContract.InteractorCallback {

    override fun getShips(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getShips(id, api, this)
    }

    override fun cancelRequest() = interactor.cancelRequest()

    override fun onSuccess(launch: Launch?) {
        launch?.ships?.let {
            view.updateShipsRecyclerView(it)
        }
        view.hideProgress()
    }

    override fun onError(error: String) {
        view.showError(error)
    }

}