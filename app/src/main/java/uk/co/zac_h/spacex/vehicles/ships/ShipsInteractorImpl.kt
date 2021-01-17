package uk.co.zac_h.spacex.vehicles.ships

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class ShipsInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Ship>?> {

    private var call: Call<ShipsDocsModel>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Ship>?>
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