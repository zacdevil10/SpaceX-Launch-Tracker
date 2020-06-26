package uk.co.zac_h.spacex.vehicles.capsules

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CapsulesDocsModel
import uk.co.zac_h.spacex.model.spacex.QueryModel
import uk.co.zac_h.spacex.model.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.model.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CapsulesInteractorImpl : BaseNetwork(), CapsulesContract.CapsulesInteractor {

    private var call: Call<CapsulesDocsModel>? = null

    override fun getCapsules(api: SpaceXInterface, listener: CapsulesContract.InteractorCallback) {
        val populateList: ArrayList<QueryPopulateModel> = ArrayList()

        populateList.add(
            QueryPopulateModel(
                "launches",
                select = listOf("name", "flight_number"),
                populate = ""
            )
        )

        val query = QueryModel("", QueryOptionsModel(false, populateList, "", ""))

        call = api.getCapsules(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}