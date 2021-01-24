package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.Launch
import java.util.concurrent.TimeUnit

class DashboardWearPresenterImpl(
    private val view: DashboardWearView,
    private val interactor: DashboardWearInteractor
) : DashboardWearPresenter, DashboardWearInteractor.Callback {

    private lateinit var next: Launch
    private lateinit var latest: Launch

    override fun getLaunch(id: String) {
        when (id) {
            "next" -> {
                if (!::next.isInitialized) {
                    view.showNextProgress()
                    interactor.getSingleLaunch(id, this)
                } else {
                    onNextSuccess(next)
                }
            }
            "latest" -> {
                if (!::latest.isInitialized) {
                    view.showLatestProgress()
                    interactor.getSingleLaunch(id, this)
                } else {
                    onLatestSuccess(latest)
                }
            }
        }
    }

    override fun updateCountdown(time: Long) {
        val remaining = String.format(
            "%02d:%02d:%02d:%02d",
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

    override fun onNextSuccess(launch: Launch?) {
        launch?.let {
            if (!::next.isInitialized) next = launch

            view.apply {
                updateNextLaunch(it)
                hideNextProgress()
                setCountdown(it.launchDate?.dateUnix)
            }
        }
    }

    override fun onLatestSuccess(launch: Launch?) {
        launch?.let {
            if (!::latest.isInitialized) latest = launch

            view.apply{
                updateLatestLaunch(it)
                hideLatestProgress()
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}