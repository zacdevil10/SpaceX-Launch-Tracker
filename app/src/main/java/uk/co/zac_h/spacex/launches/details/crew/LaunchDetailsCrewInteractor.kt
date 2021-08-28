package uk.co.zac_h.spacex.launches.details.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchDetailsCrewInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Crew>> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Crew>>
    ) {
        val populateList = listOf(
            QueryPopulateModel(
                "crew.crew",
                populate = listOf(
                    QueryPopulateModel(
                        "launches",
                        populate = "",
                        select = listOf("flight_number", "name", "date_unix")
                    )
                ),
                select = ""
            )
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(data as String),
            QueryOptionsModel(false, populateList, "", listOf("crew"), 1)
        )

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)?.crew
                        ?.map { Crew(it) }
                        ?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}