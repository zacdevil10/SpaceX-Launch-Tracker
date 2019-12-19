package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.model.spacex.CoreModel

interface CoreView {

    fun updateCores(cores: List<CoreModel>)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeRefresh(refreshing: Boolean)

    fun showError(error: String)
}