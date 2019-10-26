package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.LaunchesModel

interface LaunchDetailsView {

    fun updateLaunchDataView(launch: LaunchesModel?)

    fun openWebLink(link: String)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)
}