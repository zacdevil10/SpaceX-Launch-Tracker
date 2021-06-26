package uk.co.zac_h.spacex.about.company

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.Company
import uk.co.zac_h.spacex.model.spacex.CompanyResponse
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CompanyInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<Company> {

    private var call: Call<CompanyResponse>? = null

    override fun get(api: SpaceXInterface, listener: NetworkInterface.Callback<Company>) {
        call = api.getCompanyInfo().apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.let { Company(it) }?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}