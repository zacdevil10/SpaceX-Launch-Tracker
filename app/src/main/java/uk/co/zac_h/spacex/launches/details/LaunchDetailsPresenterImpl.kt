package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsView,
    private val interactor: LaunchDetailsInteractor
) : LaunchDetailsPresenter,
    LaunchDetailsInteractor.InteractorCallback {

    override fun getLaunch(id: String) {
        interactor.getSingleLaunch(id, this)
    }

    override fun addLaunchModel(launchModel: LaunchesModel?) {
        view.updateLaunchDataView(launchModel)
    }

    override fun onSuccess(launchesModel: LaunchesModel?) {
        view.updateLaunchDataView(launchesModel)
    }

    override fun onError(error: String) {

    }
}