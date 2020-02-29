package uk.co.zac_h.spacex.vehicles.cores

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CorePresenter {

    fun getCores(api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequest()
}