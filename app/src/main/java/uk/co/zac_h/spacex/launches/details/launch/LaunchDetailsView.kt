package uk.co.zac_h.spacex.launches.details.launch

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchDetailsView {

    fun updateLaunchDataView(launch: LaunchesModel?)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)
}