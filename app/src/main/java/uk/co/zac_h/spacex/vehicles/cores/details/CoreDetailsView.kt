package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel

interface CoreDetailsView {

    fun updateCoreDetails(coreModel: CoreModel)

    fun showProgress()

    fun hideProgress()

    fun showError(error: String)
}