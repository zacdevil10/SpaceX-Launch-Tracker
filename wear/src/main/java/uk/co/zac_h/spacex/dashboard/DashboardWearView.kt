package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface DashboardWearView {

    fun updateNextLaunch(launch: LaunchesModel)

    fun updateLatestLaunch(launch: LaunchesModel)

    fun setCountdown(launchDateUnix: Long)

    fun updateCountdown(countdown: String)

    fun showNextProgress()

    fun showLatestProgress()

    fun hideNextProgress()

    fun hideLatestProgress()

    fun showError(error: String)
}