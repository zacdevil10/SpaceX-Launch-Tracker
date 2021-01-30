package uk.co.zac_h.spacex.crew

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CrewInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Crew>?> {

    private var call: Call<CrewDocsModel>? = null

    override fun get(api: SpaceXInterface, listener: NetworkInterface.Callback<List<Crew>?>) {
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

        call = api.queryCrewMembers(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { Crew(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}