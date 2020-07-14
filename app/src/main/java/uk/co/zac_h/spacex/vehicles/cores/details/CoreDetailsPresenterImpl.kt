package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreExtendedModel

class CoreDetailsPresenterImpl(
    private val view: CoreDetailsContract.CoreDetailsView
) : CoreDetailsContract.CoreDetailsPresenter {

    override fun addCoreModel(coreModel: CoreExtendedModel) {
        view.updateCoreDetails(coreModel)
    }

}