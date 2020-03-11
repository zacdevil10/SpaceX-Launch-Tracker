package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import java.util.concurrent.TimeUnit

class DashboardPresenterImpl(
    private val view: DashboardView,
    private val prefs: PinnedSharedPreferencesHelper,
    private val interactor: DashboardInteractor
) : DashboardPresenter,
    DashboardInteractor.InteractorCallback {

    private val pinnedLaunches = LinkedHashMap<String, LaunchesModel>()

    override fun getLatestLaunches(api: SpaceXInterface) {
        view.showProgress()
        view.showPinnedMessage()

        pinnedLaunches.clear()

        interactor.apply {
            getSingleLaunch("next", api, this@DashboardPresenterImpl)
            getSingleLaunch("latest", api, this@DashboardPresenterImpl)
        }

        if (pinnedLaunches.isEmpty()) {
            interactor.apply {
                prefs.getAllPinnedLaunches()?.forEach {
                    if (it.value as Boolean) getSingleLaunch(
                        it.key,
                        api,
                        this@DashboardPresenterImpl
                    )
                }
            }
        }
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

    override fun onSuccess(id: String, launchesModel: LaunchesModel?) {
        launchesModel?.let { launchModel ->
            when (id) {
                "next" -> {
                    view.updateNextLaunch(launchModel)
                    launchModel.let { launch ->
                        launch.tbd?.let {
                            if (!it) {
                                view.apply {
                                    setCountdown(launch.launchDateUnix)
                                    showCountdown()
                                }
                            } else {
                                view.hideCountdown()
                            }
                        }
                    }
                }
                "latest" -> view.updateLatestLaunch(launchModel)
                else -> {
                    pinnedLaunches[id] = launchModel
                    view.hidePinnedMessage()
                    view.updatePinnedList(pinnedLaunches)
                }
            }
        }

        if (!interactor.hasActiveRequest()) view.hideProgress()
        view.toggleSwipeProgress(false)
    }

    override fun onError(error: String) {
        view.apply {
            showError(error)
            toggleSwipeProgress(false)
        }
    }
}