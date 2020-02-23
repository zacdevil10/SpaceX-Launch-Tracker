package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface LaunchDetailsPresenter {

    fun getLaunch(id: String, api: SpaceXInterface = SpaceXInterface.create())

    fun addLaunchModel(launchModel: LaunchesModel?)

    fun pinLaunch(id: String, pin: Boolean)

    fun isPinned(id: String): Boolean

    fun createEvent()

    fun cancelRequest()
}