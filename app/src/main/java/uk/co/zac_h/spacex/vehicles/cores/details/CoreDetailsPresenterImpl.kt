package uk.co.zac_h.spacex.vehicles.cores.details

import uk.co.zac_h.spacex.model.spacex.CoreModel
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CoreDetailsPresenterImpl(
    private val view: CoreDetailsView,
    private val interactor: CoreDetailsInteractor
) : CoreDetailsPresenter,
    CoreDetailsInteractor.InteractorCallback {

    override fun getCoreDetails(serial: String, api: SpaceXInterface) {
        view.showProgress()
        interactor.getCoreDetails(serial, api, this)
    }

    override fun addCoreModel(coreModel: CoreModel) {
        view.updateCoreDetails(coreModel)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(coreModel: CoreModel?) {
        coreModel?.let {
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