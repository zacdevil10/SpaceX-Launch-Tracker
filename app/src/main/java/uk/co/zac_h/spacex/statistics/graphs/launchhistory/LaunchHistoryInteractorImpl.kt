package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchHistoryInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Launch>?>
    ) {
        val populateList = listOf(
            QueryPopulateModel("rocket", populate = "", select = listOf("success_rate_pct"))
        )

        val query = QueryModel(
            QueryUpcomingLaunchesModel(false),
            QueryOptionsModel(false, populateList, "", listOf("rocket", "success"), 100000)
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