package uk.co.zac_h.spacex.launches.details.core

import uk.co.zac_h.spacex.model.CoreModel

interface CoreDetailsView {

    fun updateCoreMissionsList(coreModel: CoreModel)

    fun updateCoreStats(coreModel: CoreModel)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)
}