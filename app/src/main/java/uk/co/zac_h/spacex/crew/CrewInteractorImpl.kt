package uk.co.zac_h.spacex.crew

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.CrewDocsModel
import uk.co.zac_h.spacex.model.spacex.QueryModel
import uk.co.zac_h.spacex.model.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.model.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CrewInteractorImpl : BaseNetwork(), CrewContract.CrewInteractor {

    private var call: Call<CrewDocsModel>? = null

    override fun getCrew(api: SpaceXInterface, listener: CrewContract.InteractorCallback) {
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

        call = api.getCrew(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}