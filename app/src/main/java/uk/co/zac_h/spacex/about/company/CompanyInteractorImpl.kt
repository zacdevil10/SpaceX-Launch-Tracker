package uk.co.zac_h.spacex.about.company

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.model.spacex.CompanyResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CompanyInteractorImpl : BaseNetwork(), CompanyContract.CompanyInteractor {

    private var call: Call<CompanyResponse>? = null

    override fun getCompanyInfo(
        api: SpaceXInterface,
        listener: CompanyContract.InteractorCallback
    ) {
        call = api.getCompanyInfo().apply {
            makeCall {
                onResponseSuccess = {
                    listener.onSuccess(it.body()?.let { response -> Company(response) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}