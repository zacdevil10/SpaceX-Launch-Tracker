package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreExtendedModel

interface CoreDetailsContract {

    interface CoreDetailsView {
        fun updateCoreDetails(coreModel: CoreExtendedModel)
        fun showProgress()
        fun hideProgress()
        fun showError(error: String)
    }

    interface CoreDetailsPresenter {
        fun addCoreModel(coreModel: CoreExtendedModel)
    }
}