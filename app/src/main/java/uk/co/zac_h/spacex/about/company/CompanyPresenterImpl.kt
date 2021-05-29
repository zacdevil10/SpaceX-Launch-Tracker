package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.rest.SpaceXInterface

class CompanyPresenterImpl(
    private val view: NetworkInterface.View<Company>,
    private val interactor: NetworkInterface.Interactor<Company?>
) : NetworkInterface.Presenter<Company?>, NetworkInterface.Callback<Company?> {

    override fun getOrUpdate(response: Company?, api: SpaceXInterface) {
        view.showProgress()
        response?.let { onSuccess(it) } ?: interactor.get(api, this)
    }

    override fun cancelRequest() {
        interactor.cancelAllRequests()
    }

    override fun onSuccess(response: Company?) {
        response?.let {
            view.apply {
                update(it)
                hideProgress()
            }
        }
    }

    override fun onError(error: String) {
        view.showError(error)
    }
}