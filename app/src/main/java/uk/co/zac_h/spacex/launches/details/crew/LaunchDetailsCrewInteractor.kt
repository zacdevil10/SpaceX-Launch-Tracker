package uk.co.zac_h.spacex.launches.details.crew

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.utils.SPACEX_FIELD_CREW_LAUNCHES

class LaunchDetailsCrewInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Crew>> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Crew>>
    ) {
        val populateList = listOf(
            QueryPopulateModel(
                "crew.crew",
                populate = listOf(
                    QueryPopulateModel(
                        "crew.launches",
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

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)?.crew
                        ?.map { Crew(it) }
                        ?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}