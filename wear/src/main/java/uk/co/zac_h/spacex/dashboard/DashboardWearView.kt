package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.model.spacex.Launch

interface DashboardWearView {

    fun updateNextLaunch(launch: Launch)

    fun updateLatestLaunch(launch: Launch)

    fun setCountdown(launchDateUnix: Long?)

    fun updateCountdown(countdown: String)

    fun showNextProgress()

    fun showLatestProgress()

    fun hideNextProgress()

    fun hideLatestProgress()

    fun showError(error: String)
}