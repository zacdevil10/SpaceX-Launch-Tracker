package uk.co.zac_h.spacex.vehicles.cores

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class CoreInteractorImpl : BaseNetwork(), VehiclesContract.Interactor<Core> {

    private var call: Call<CoreDocsModel>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<Core>
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