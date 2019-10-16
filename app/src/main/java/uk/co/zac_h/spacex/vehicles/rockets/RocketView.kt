package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.model.RocketsModel

interface RocketView {

    fun updateRockets(rockets: List<RocketsModel>)

    fun showProgress()

    fun hideProgress()

    fun error(error: String)
}