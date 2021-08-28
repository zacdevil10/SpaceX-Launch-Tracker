package uk.co.zac_h.spacex.launches.details.cores

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchDetailsCoresInteractor : BaseNetwork(), NetworkInterface.Interactor<List<LaunchCore>> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<LaunchCore>>
    ) {
        val populateList = listOf(
            QueryPopulateModel(
                "cores",
                populate = listOf(
                    QueryPopulateModel(
                        "core",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                select = listOf("name", "flight_number"),
                                populate = ""
                            )
                        ),
                        select = ""
                    ),
                    QueryPopulateModel(
                        "landpad",
                        select = "",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                select = listOf("id"),
                                populate = ""
                            )
                        )
                    )
                ),
                select = ""
            )
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(data as String),
            QueryOptionsModel(false, populateList, "", listOf("cores"), 10)
        )

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)
                        ?.let { Launch(it) }
                        ?.let {
                            it.cores?.let { cores -> listener.onSuccess(cores) }
                        }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()

}