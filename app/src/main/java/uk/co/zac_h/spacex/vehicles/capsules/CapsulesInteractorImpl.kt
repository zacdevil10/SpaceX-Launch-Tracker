package uk.co.zac_h.spacex.vehicles.capsules

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class CapsulesInteractorImpl : BaseNetwork(), VehiclesContract.Interactor<CapsulesModel> {

    private var call: Call<CapsulesDocsModel>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<CapsulesModel>
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

        call = api.getCapsules(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()?.docs) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}