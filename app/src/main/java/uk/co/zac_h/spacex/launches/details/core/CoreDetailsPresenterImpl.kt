package uk.co.zac_h.spacex.launches.details.core

import uk.co.zac_h.spacex.model.CoreModel

class CoreDetailsPresenterImpl(
    private val view: CoreDetailsView,
    private val interactor: CoreDetailsInteractor
) : CoreDetailsPresenter, CoreDetailsInteractor.InteractorCallback {

    private lateinit var coreDetails: CoreModel

    override fun getCoreDetails(serial: String) {
        if (!::coreDetails.isInitialized) {
            view.showProgress()
            interactor.getCoreDetails(serial, this)
        } else {
            onSuccess(coreDetails)
        }
    }

    override fun onSuccess(coreModel: CoreModel?) {
        coreModel?.let {
            coreDetails = coreModel

            view.apply {
                hideProgress()
                updateCoreMissionsList(coreModel)
                updateCoreStats(coreModel)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}