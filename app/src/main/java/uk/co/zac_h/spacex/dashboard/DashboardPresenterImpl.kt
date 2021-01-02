package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.Launch
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.util.concurrent.TimeUnit

class DashboardPresenterImpl(
    private val view: DashboardContract.DashboardView,
    private val interactor: DashboardContract.DashboardInteractor
) : DashboardContract.DashboardPresenter,
    DashboardContract.InteractorCallback {

    override fun getLatestLaunches(
        next: Launch?,
        latest: Launch?,
        api: SpaceXInterface
    ) {
        view.showPinnedMessage()
    }

    override fun getSingleLaunch(id: String, api: SpaceXInterface) {
        interactor.getSingleLaunch(id, api, this)
    }

    override fun updateCountdown(time: Long) {

    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun toggleNextLaunchVisibility(visible: Boolean) {

    }

    override fun toggleLatestLaunchVisibility(visible: Boolean) {

    }

    override fun togglePinnedList(visible: Boolean) {

    }

    override fun onSuccess(id: String, launchModel: Launch?) {
        launchModel?.let { launch ->
            when (id) {
                "next" -> {

                }
                "latest" -> {

                }
                else -> {
                    view.hidePinnedMessage()
                    view.updatePinnedList(id, launch)
                }
            }
        }
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }
}