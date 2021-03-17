package uk.co.zac_h.spacex.launches.details.details

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper

class LaunchDetailsPresenterImpl(
    private val view: LaunchDetailsContract.LaunchDetailsView,
    private val helper: PinnedSharedPreferencesHelper,
    private val interactor: NetworkInterface.Interactor<Launch?>
) : LaunchDetailsContract.LaunchDetailsPresenter,
    NetworkInterface.Callback<Launch?> {

    override fun get(data: Any, api: SpaceXInterface) {
        view.toggleSwipeRefresh(true)
        interactor.get(data, api, this)
    }

    override fun addLaunchModel(launch: Launch?, isExt: Boolean) {
        view.update(isExt, launch)
    }

    override fun pinLaunch(id: String, pin: Boolean) {
        helper.setPinnedLaunch(id, pin)
    }

    override fun isPinned(id: String): Boolean = helper.isPinned(id)

    override fun createEvent() {
        view.newCalendarEvent()
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: Launch?) {
        response?.let { launch ->
            view.apply {
                update(true, launch)
                toggleSwipeRefresh(false)
            }
        }
    }

    override fun onError(error: String) {
        view.apply {
            toggleSwipeRefresh(false)
            showError(error)
        }
    }
}