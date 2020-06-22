package uk.co.zac_h.spacex.vehicles.cores

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CoreInteractorImpl : BaseNetwork(), CoreContract.CoreInteractor {

    private var call: Call<CoreDocsModel>? = null

    override fun getCores(api: SpaceXInterface, listener: CoreContract.InteractorCallback) {
        val populateList: ArrayList<QueryPopulateModel> = ArrayList()

        populateList.add(
            QueryPopulateModel(
                "launches",
                select = QueryCoreSelectModel(1, 1),
                populate = ""
            )
        )

        val query = QueryModel("", QueryOptionsModel(false, populateList, "", ""))

        call = api.getCores(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}