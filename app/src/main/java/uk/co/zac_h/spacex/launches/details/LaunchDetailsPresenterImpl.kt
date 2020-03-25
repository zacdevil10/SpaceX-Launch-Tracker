package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsView,
    private val helper: PinnedSharedPreferencesHelper,
    private val interactor: LaunchDetailsInteractor
) : LaunchDetailsPresenter,
    LaunchDetailsInteractor.InteractorCallback {

    override fun getLaunch(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getSingleLaunch(id, api, this)
    }

    override fun addLaunchModel(launchModel: LaunchesModel?) {
        view.updateLaunchDataView(launchModel)
    }

    override fun pinLaunch(id: String, pin: Boolean) {
        helper.setPinnedLaunch(id, pin)
    }

    override fun isPinned(id: String): Boolean = helper.isPinned(id)

    override fun createEvent() {
        view.newCalendarEvent()
    }

    override fun cancelRequest() {
        interactor.cancelRequest()
    }

    override fun onSuccess(launchModel: LaunchesModel?) {
        view.apply {
            hideProgress()
            updateLaunchDataView(launchModel)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}