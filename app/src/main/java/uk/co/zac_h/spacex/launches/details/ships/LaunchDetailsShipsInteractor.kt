package uk.co.zac_h.spacex.launches.details.ships

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchDetailsShipsInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Ship>> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Ship>>
    ) {
        val query = QueryModel(
            QueryLaunchesQueryModel(data as String),
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "ships",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                populate = "",
                                select = listOf("name", "flight_number")
                            )
                        ),
                        select = ""
                    )
                ), "", listOf("ships"), 1
            )
        )

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)
                        ?.let { Launch(it) }
                        ?.ships
                        ?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}