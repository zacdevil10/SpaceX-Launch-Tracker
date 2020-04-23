package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.PinnedSharedPreferencesHelper
import java.util.concurrent.TimeUnit

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

    override fun cancelRequest() {
        interactor.cancelRequest()
    }

    override fun onSuccess(launchModel: LaunchesModel?) {
        view.apply {
            hideProgress()
            updateLaunchDataView(launchModel)
            launchModel?.let { launch ->
                launch.tbd?.let {
                    val time = launch.launchDateUnix.times(1000) - System.currentTimeMillis()
                    if (!it && time >= 0) {
                        setCountdown(time)
                        showCountdown()
                    } else {
                        hideCountdown()
                    }
                }
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}