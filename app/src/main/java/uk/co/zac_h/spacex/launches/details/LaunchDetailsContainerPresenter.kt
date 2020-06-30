package uk.co.zac_h.spacex.launches.details

import java.util.concurrent.TimeUnit

class LaunchDetailsContainerPresenter(private val view: LaunchDetailsContainerContract.View) :
    LaunchDetailsContainerContract.Presenter {

    override fun startCountdown(launchDateUnix: Long?, tbd: Boolean?) {
        val time = (launchDateUnix?.times(1000) ?: 0) - System.currentTimeMillis()
        view.apply {
            tbd?.let {
                if (!tbd && time >= 0) {
                    setCountdown(time)
                    showCountdown()
                } else {
                    hideCountdown()
                }
            } ?: run {
                hideCountdown()
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

}