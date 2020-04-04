package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CompanyInteractorImpl(api: SpaceXInterface = SpaceXInterface.create()) : BaseNetwork(),
    CompanyInteractor {

    private var call = api.getCompanyInfo()

    override fun getCompanyInfo(listener: CompanyInteractor.Callback) {
        call.makeCall {
            onResponseSuccess = { listener.onSuccess(it.body()) }
            onResponseFailure = { listener.onError(it) }
        }
    }

    override fun cancelAllRequests() = call.cancel()
}