package uk.co.zac_h.spacex.crew

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class CrewInteractorImpl : BaseNetwork(), CrewContract.CrewInteractor {

    private var call: Call<CrewDocsModel>? = null

    override fun getCrew(api: SpaceXInterface, listener: CrewContract.InteractorCallback) {
        val populateList: ArrayList<QueryPopulateModel> = ArrayList()

        populateList.add(QueryPopulateModel("launches", QueryCrewModel(1, 1)))

        val query = QueryModel(QueryOptionsModel(false, populateList))

        call = api.getCrew(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}