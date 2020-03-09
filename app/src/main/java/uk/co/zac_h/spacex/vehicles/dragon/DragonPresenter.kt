package uk.co.zac_h.spacex.vehicles.dragon

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface DragonPresenter {

    fun getDragon(api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequest()

}