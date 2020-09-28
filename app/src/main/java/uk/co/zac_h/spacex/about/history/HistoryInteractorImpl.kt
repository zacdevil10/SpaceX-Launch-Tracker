package uk.co.zac_h.spacex.about.history

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.HistoryDocsModel
import uk.co.zac_h.spacex.model.spacex.QueryHistorySort
import uk.co.zac_h.spacex.model.spacex.QueryModel
import uk.co.zac_h.spacex.model.spacex.QueryOptionsModel
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

        call = api.getHistory(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()?.docs) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}