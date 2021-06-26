package uk.co.zac_h.spacex.about.history

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class HistoryInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<History>> {

    private var call: Call<HistoryDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<History>>
    ) {
        val query = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                QueryHistorySort(data as String),
                "",
                1000000
            )
        )

        call = api.queryHistory(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.map { History(it) }?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}