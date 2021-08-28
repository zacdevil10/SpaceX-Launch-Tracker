package uk.co.zac_h.spacex.crew

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Crew
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class CrewInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Crew>> {

    //private var call: Call<CrewDocsModel>? = null

    override fun get(api: SpaceXService, listener: NetworkInterface.Callback<List<Crew>>) {
        val query = QueryModel(
            "",
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "launches",
                        select = listOf("flight_number", "name", "date_unix"),
                        populate = ""
                    )
                ), "", "", 100000
            )
        )

        /*call = api.queryCrewMembers(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.map { Crew(it) }?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}