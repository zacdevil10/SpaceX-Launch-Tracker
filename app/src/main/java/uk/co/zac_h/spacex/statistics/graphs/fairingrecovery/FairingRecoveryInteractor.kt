package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class FairingRecoveryInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(api: SpaceXInterface, listener: NetworkInterface.Callback<List<Launch>?>) {
        val query = QueryModel(
            QueryFairingRecovery(true),
            QueryOptionsModel(
                false,
                "",
                QueryLaunchesSortByDate("asc"),
                listOf("fairings", "date_unix"),
                1000000
            )
        )

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(true, response.body()?.docs?.map { Launch(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}