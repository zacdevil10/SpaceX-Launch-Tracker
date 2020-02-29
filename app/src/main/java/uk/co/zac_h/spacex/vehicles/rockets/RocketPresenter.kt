package uk.co.zac_h.spacex.vehicles.rockets

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface RocketPresenter {

    fun getRockets(api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequest()
}