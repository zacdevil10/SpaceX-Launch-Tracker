package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

interface CoreDetailsPresenter {

    fun getCoreDetails(serial: String, api: SpaceXInterface = SpaceXInterface.create())

    fun addCoreModel(coreModel: CoreModel)

    fun cancelRequest()
}