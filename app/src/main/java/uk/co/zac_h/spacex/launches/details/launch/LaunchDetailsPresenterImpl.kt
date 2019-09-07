package uk.co.zac_h.spacex.launches.details.launch

import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsView,
    private val interactor: LaunchDetailsInteractor
) : LaunchDetailsPresenter,
    LaunchDetailsInteractor.InteractorCallback {

    override fun getLaunch(id: String) {
        view.showProgress()
        interactor.getSingleLaunch(id, this)
    }

    override fun addLaunchModel(launchModel: LaunchesModel?) {
        view.updateLaunchDataView(launchModel)
    }

    override fun onSuccess(launchesModel: LaunchesModel?) {
        view.hideProgress()
        view.updateLaunchDataView(launchesModel)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}