package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel

interface CoreDetailsPresenter {

    fun addCoreModel(coreModel: CoreModel)

    fun getCoreDetails(serial: String)

    fun cancelRequest()
}