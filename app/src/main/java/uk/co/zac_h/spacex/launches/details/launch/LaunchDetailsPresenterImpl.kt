package uk.co.zac_h.spacex.launches.details.launch

import uk.co.zac_h.spacex.model.LaunchesModel

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsView,
    private val helper: LaunchDetailsHelper,
    private val interactor: LaunchDetailsInteractor
) : LaunchDetailsPresenter,
    LaunchDetailsInteractor.InteractorCallback {

    private var launchModel: LaunchesModel? = null

    override fun getLaunch(id: String) {
        view.showProgress()
        interactor.getSingleLaunch(id, this)
    }

    override fun addLaunchModel(launchModel: LaunchesModel?) {
        this.launchModel = launchModel
        view.updateLaunchDataView(launchModel)
    }

    override fun pinLaunch(pin: Boolean) {
        when (pin) {
            true -> launchModel?.flightNumber?.let { helper.pinLaunch(it) }
            false -> launchModel?.flightNumber?.let { helper.removePinnedLaunch(it) }
        }
    }

    override fun isPinned(): Boolean =
        launchModel?.flightNumber?.let { helper.isPinned(it) } ?: false

    override fun isPinned(id: Int): Boolean = helper.isPinned(id)

    override fun onSuccess(launchModel: LaunchesModel?) {
        this.launchModel = launchModel
        view.hideProgress()
        view.updateLaunchDataView(launchModel)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}