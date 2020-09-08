package uk.co.zac_h.spacex.about.company

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CompanyModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CompanyInteractorImpl : BaseNetwork(), CompanyContract.CompanyInteractor {

    private var call: Call<CompanyModel>? = null

    override fun getCompanyInfo(
        api: SpaceXInterface,
        listener: CompanyContract.InteractorCallback
    ) {
        call = api.getCompanyInfo().apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}