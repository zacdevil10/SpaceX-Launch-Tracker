package uk.co.zac_h.spacex.dashboard

import uk.co.zac_h.spacex.utils.data.LaunchesModel

interface DashboardView {

    fun updateLaunchesList(launchesModel: LaunchesModel?)
}