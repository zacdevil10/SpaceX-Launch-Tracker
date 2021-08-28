package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class FairingRecoveryInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(api: SpaceXService, listener: NetworkInterface.Callback<List<Launch>?>) {
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

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(true, response.body()?.docs?.map { Launch(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}