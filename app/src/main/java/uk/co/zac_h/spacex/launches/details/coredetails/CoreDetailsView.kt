package uk.co.zac_h.spacex.launches.details.coredetails

import uk.co.zac_h.spacex.utils.data.CoreModel

interface CoreDetailsView {

    fun updateCoreMissionsList(coreModel: CoreModel?)

    fun updateCoreStats(coreModel: CoreModel?)

    fun toggleProgress(visibility: Int)

    fun showError(error: String)
}