package uk.co.zac_h.spacex.vehicles.ships

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.dto.spacex.Ship
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class ShipsInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Ship>?> {

    //private var call: Call<ShipsDocsModel>? = null

    override fun get(
        api: SpaceXService,
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

        /*call = api.queryShips(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { Ship(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}