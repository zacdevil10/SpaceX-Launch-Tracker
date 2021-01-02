package uk.co.zac_h.spacex.launches.details.crew

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsCrewInteractor : BaseNetwork(), LaunchDetailsCrewContract.Interactor {

    private var call: Call<LaunchDocsModel>? = null

    override fun getCrew(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsCrewContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel(
                "crew",
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
            QueryLaunchesQueryModel(id),
            QueryOptionsModel(false, populateList, "", listOf("crew"), 1)
        )

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.get(0)?.crew?.map { Crew(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelRequest() = terminateAll()
}