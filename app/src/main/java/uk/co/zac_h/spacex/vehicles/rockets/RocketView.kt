package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.spacex.RocketsModel

interface RocketView {

    fun updateRockets(rockets: List<RocketsModel>)

    fun showProgress()

    fun hideProgress()

    fun toggleSwipeRefresh(refreshing: Boolean)

    fun showError(error: String)
}