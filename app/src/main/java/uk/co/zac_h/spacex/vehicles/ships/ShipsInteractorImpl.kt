package uk.co.zac_h.spacex.vehicles.ships

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.vehicles.VehiclesContract

class ShipsInteractorImpl : BaseNetwork(), VehiclesContract.Interactor<ShipExtendedModel> {

    private var call: Call<ShipsDocsModel>? = null

    override fun getVehicles(
        api: SpaceXInterface,
        listener: VehiclesContract.InteractorCallback<ShipExtendedModel>
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
                limit = 50
            )
        )

        call = api.getShips(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()?.docs) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}