package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.model.spacex.LaunchesModel

interface LaunchDetailsPresenter {

    fun getLaunch(id: String)

    fun addLaunchModel(launchModel: LaunchesModel?)

    fun pinLaunch(pin: Boolean)

    fun isPinned(): Boolean

    fun isPinned(id: Int): Boolean

    fun createEvent()

    fun cancelRequest()
}