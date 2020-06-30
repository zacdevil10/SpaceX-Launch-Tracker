package uk.co.zac_h.spacex.launches.details.details

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedDocsModel
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsContract.LaunchDetailsView,
    private val helper: PinnedSharedPreferencesHelper,
    private val interactor: LaunchDetailsContract.LaunchDetailsInteractor
) : LaunchDetailsContract.LaunchDetailsPresenter,
    LaunchDetailsContract.InteractorCallback {

    override fun getLaunch(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getSingleLaunch(id, api, this)
    }

    override fun addLaunchModel(launchModel: LaunchesExtendedModel?, isExt: Boolean) {
        view.updateLaunchDataView(launchModel, isExt)
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

    override fun onSuccess(launchModel: LaunchesExtendedDocsModel?) {
        view.apply {
            hideProgress()
            launchModel?.docs?.get(0)?.let { launch ->
                updateLaunchDataView(launch, true)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}