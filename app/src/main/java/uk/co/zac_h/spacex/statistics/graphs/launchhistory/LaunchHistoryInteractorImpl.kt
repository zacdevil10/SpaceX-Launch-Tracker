package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchHistoryInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Launch>?>
    ) {
        val populateList = listOf(
            QueryPopulateModel("rocket", populate = "", select = listOf("success_rate_pct"))
        )

        val query = QueryModel(
            QueryUpcomingLaunchesModel(false),
            QueryOptionsModel(false, populateList, "", listOf("rocket", "success"), 100000)
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