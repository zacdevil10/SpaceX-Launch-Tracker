package uk.co.zac_h.spacex.vehicles.ships

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class ShipsInteractorImpl : BaseNetwork(), VehiclesContract.Interactor<Ship> {

    private var call: Call<ShipsDocsModel>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<Ship>
    ) {
        val query = QueryModel(
            query = "",
            options = QueryOptionsModel(
                false,
                populate = listOf(
                    QueryPopulateModel(
                        path = "launches",
                        select = listOf("flight_number", "name"),
                        populate = ""
                    )
                ),
                sort = "",
                select = "",
                limit = 200
            )
        )

        call = api.queryShips(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { Ship(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}