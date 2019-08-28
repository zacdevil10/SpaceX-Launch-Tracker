package uk.co.zac_h.spacex.launches.details.coredetails

import uk.co.zac_h.spacex.utils.data.CoreModel

class CoreDetailsPresenterImpl(
    private val view: CoreDetailsView,
    private val interactor: CoreDetailsInteractor
) : CoreDetailsPresenter, CoreDetailsInteractor.InteractorCallback {

    override fun getCoreDetails(serial: String?) {
        view.showProgress()
        serial?.let {
            interactor.getCoreDetails(serial, this)
        }
    }

    override fun onSuccess(coreModel: CoreModel?) {
        view.apply {
            hideProgress()
            updateCoreMissionsList(coreModel)
            updateCoreStats(coreModel)
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}