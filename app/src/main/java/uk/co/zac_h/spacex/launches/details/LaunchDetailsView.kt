package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchDetailsView {

    fun updateLaunchDataView(launch: LaunchesModel?)

    fun toggleProgress(visibility: Int)

    fun showError(error: String)
}