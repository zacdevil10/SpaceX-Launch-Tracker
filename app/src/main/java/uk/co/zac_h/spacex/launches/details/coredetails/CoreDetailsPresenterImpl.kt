package uk.co.zac_h.spacex.launches.details.coredetails

import android.view.View
import uk.co.zac_h.spacex.utils.data.CoreModel

class CoreDetailsPresenterImpl(
    private val view: CoreDetailsView,
    private val interactor: CoreDetailsInteractor
) : CoreDetailsPresenter, CoreDetailsInteractor.InteractorCallback {

    override fun getCoreDetails(serial: String?) {
        view.toggleProgress(View.VISIBLE)
        serial?.let {
            interactor.getCoreDetails(serial, this)
        }
    }

    override fun onSuccess(coreModel: CoreModel?) {
        view.toggleProgress(View.INVISIBLE)
        view.updateCoreMissionsList(coreModel)
        view.updateCoreStats(coreModel)
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}