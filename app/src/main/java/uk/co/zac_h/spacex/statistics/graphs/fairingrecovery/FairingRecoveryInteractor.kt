package uk.co.zac_h.spacex.statistics.graphs.fairingrecovery

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class FairingRecoveryInteractor : BaseNetwork(), FairingRecoveryContract.Interactor {

    private var call: Call<LaunchDocsModel>? = null

    override fun getLaunches(api: SpaceXInterface, listener: FairingRecoveryContract.Callback) {
        val query = QueryModel(
            QueryFairingRecovery(true),
            QueryOptionsModel(
                false,
                "",
                QueryLaunchesSortByDate("asc"),
                listOf("fairings", "date_local", "date_unix"),
                1000000
            )
        )

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body(), true) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}