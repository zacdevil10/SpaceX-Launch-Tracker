package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import java.util.concurrent.TimeUnit

class DashboardPresenterImpl(
    private val view: DashboardContract.DashboardView,
    private val interactor: DashboardContract.DashboardInteractor
) : DashboardContract.DashboardPresenter,
    DashboardContract.InteractorCallback {

    override fun getLatestLaunches(
        next: LaunchesExtendedModel?,
        latest: LaunchesExtendedModel?,
        api: SpaceXInterface
    ) {
        view.showProgress()
        view.showPinnedMessage()

        interactor.apply {
            if (next == null)
                getSingleLaunch("next", api, this@DashboardPresenterImpl)
            else
                onSuccess("next", next)

            if (latest == null)
                getSingleLaunch("latest", api, this@DashboardPresenterImpl)
            else
                onSuccess("latest", latest)
        }
    }

    override fun getSingleLaunch(id: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getSingleLaunch(id, api, this)
    }

    override fun updateCountdown(time: Long) {
        val remaining = String.format(
            "T-%02d:%02d:%02d:%02d",
            TimeUnit.MILLISECONDS.toDays(time),
            TimeUnit.MILLISECONDS.toHours(time) - TimeUnit.DAYS.toHours(
                TimeUnit.MILLISECONDS.toDays(
                    time
                )
            ),
            TimeUnit.MILLISECONDS.toMinutes(time) - TimeUnit.HOURS.toMinutes(
                TimeUnit.MILLISECONDS.toHours(
                    time
                )
            ),
            TimeUnit.MILLISECONDS.toSeconds(time) - TimeUnit.MINUTES.toSeconds(
                TimeUnit.MILLISECONDS.toMinutes(
                    time
                )
            )
        )

        view.updateCountdown(remaining)
    }

    override fun cancelRequests() {
        interactor.cancelAllRequests()
    }

    override fun toggleNextLaunchVisibility(visible: Boolean) {
        view.apply {
            if (visible) showNextLaunch() else hideNextLaunch()
        }
    }

    override fun toggleLatestLaunchVisibility(visible: Boolean) {
        view.apply {
            if (visible) showLatestLaunch() else hideLatestLaunch()
        }
    }

    override fun togglePinnedList(visible: Boolean) {
        view.apply {
            if (visible) showPinnedList() else hidePinnedList()
        }
    }

    override fun onSuccess(id: String, launchModel: LaunchesExtendedModel?) {
        launchModel?.let { launch ->
            when (id) {
                "next" -> {
                    view.updateNextLaunch(launch)
                    val time =
                        (launch.launchDateUnix?.times(1000) ?: 0) - System.currentTimeMillis()
                    launch.tbd?.let { tbd ->
                        if (!tbd && time >= 0) view.apply {
                            setCountdown(time)
                            showCountdown()
                        } else view.hideCountdown()
                    } ?: run {
                        view.hideCountdown()
                    }
                }
                "latest" -> view.updateLatestLaunch(launch)
                else -> {
                    view.hidePinnedMessage()
                    view.updatePinnedList(id, launch)
                }
            }
        }

        view.hideProgress()
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }
}