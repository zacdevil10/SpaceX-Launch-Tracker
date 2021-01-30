package uk.co.zac_h.spacex.vehicles.cores

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CoreInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Core>?> {

    private var call: Call<CoreDocsModel>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Core>?>
    ) {
        val populateList: ArrayList<QueryPopulateModel> = ArrayList()

        populateList.add(
            QueryPopulateModel(
                "launches",
                select = listOf("name", "flight_number"),
                populate = ""
            )
        )

        val query = QueryModel("", QueryOptionsModel(false, populateList, "", "", 100000))

        call = api.queryCores(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { Core(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}