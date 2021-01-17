package uk.co.zac_h.spacex.statistics.graphs.landinghistory

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LandingHistoryInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Launch>?>
    ) {
        val query = QueryModel(
            QueryLandingHistory(true),
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel("cores.core", listOf("id"), ""),
                    QueryPopulateModel("cores.landpad", listOf("id"), "")
                ),
                QueryLaunchesSortByDate("asc"),
                listOf("cores", "date_local", "date_unix"),
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