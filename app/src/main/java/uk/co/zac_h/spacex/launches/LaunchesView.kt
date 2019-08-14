package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchesView {

    fun updateLaunchesList(launches: List<LaunchesModel>?)

    fun toggleProgress(visibility: Int)

    fun toggleSwipeProgress(isRefreshing: Boolean)

    fun showError(error: String)

}