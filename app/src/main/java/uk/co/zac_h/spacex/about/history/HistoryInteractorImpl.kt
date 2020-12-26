package uk.co.zac_h.spacex.about.history

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class HistoryInteractorImpl : BaseNetwork(), HistoryContract.HistoryInteractor {

    private var call: Call<HistoryDocsModel>? = null

    override fun getAllHistoricEvents(
        order: String,
        api: SpaceXInterface,
        listener: HistoryContract.InteractorCallback
    ) {
        val query = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                QueryHistorySort(order),
                "",
                1000000
            )
        )

        call = api.queryHistory(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { History(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}