package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.spacex.Launch

interface LaunchesWearView {

    fun updateLaunches(launches: List<Launch>)

    fun showProgress()

    fun hideProgress()

    fun showReload()

    fun hideReload()

    fun showError(error: String)
}