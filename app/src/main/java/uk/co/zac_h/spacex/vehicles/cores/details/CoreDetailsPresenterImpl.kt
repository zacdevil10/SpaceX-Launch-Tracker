package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.CoreModel

class CoreDetailsPresenterImpl(
    private val view: CoreDetailsView,
    private val interactor: CoreDetailsInteractor
) : CoreDetailsPresenter,
    CoreDetailsInteractor.InteractorCallback {

    private var coreDetails: CoreModel? = null

    override fun getCoreDetails(serial: String) {
        view.showProgress()
        interactor.getCoreDetails(serial, this)
    }

    override fun addCoreModel(coreModel: CoreModel) {
        coreDetails = coreModel
        view.updateCoreDetails(coreModel)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(coreModel: CoreModel?) {
        coreModel?.let {
            coreDetails = coreModel

            view.apply {
                hideProgress()
                updateCoreDetails(coreModel)
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}