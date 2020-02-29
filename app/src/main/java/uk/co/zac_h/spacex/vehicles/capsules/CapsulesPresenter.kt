package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CapsulesPresenter {

    fun getCapsules(api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequests()
}