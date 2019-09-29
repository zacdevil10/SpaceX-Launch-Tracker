package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.model.LaunchesModel

interface LaunchesWearView {

    fun updateLaunches(launches: List<LaunchesModel>)

    fun showProgress()

    fun hideProgress()

    fun showReload()

    fun hideReload()

    fun showError(error: String)
}