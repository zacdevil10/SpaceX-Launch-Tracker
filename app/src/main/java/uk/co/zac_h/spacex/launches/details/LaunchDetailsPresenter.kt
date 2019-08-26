package uk.co.zac_h.spacex.launches.details

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface LaunchDetailsPresenter {

    fun getLaunch(id: String)

    fun addLaunchModel(launchModel: LaunchesModel?)
}