package uk.co.zac_h.spacex.launches.details

import android.view.View
import uk.co.zac_h.spacex.utils.data.LaunchesModel

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsView,
    private val interactor: LaunchDetailsInteractor
) : LaunchDetailsPresenter,
    LaunchDetailsInteractor.InteractorCallback {

    override fun getLaunch(id: String) {
        view.toggleProgress(View.VISIBLE)
        interactor.getSingleLaunch(id, this)
    }

    override fun addLaunchModel(launchModel: LaunchesModel?) {
        view.updateLaunchDataView(launchModel)
    }

    override fun onSuccess(launchesModel: LaunchesModel?) {
        view.toggleProgress(View.GONE)
        view.updateLaunchDataView(launchesModel)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}