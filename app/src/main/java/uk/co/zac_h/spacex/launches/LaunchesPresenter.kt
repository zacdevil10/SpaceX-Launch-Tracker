package uk.co.zac_h.spacex.launches

import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchesPresenter {

    fun getLaunchList(id: String, api: SpaceXInterface = SpaceXInterface.create())

    fun cancelRequests()

}