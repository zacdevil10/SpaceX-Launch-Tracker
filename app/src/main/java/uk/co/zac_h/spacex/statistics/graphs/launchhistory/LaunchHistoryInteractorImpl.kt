package uk.co.zac_h.spacex.statistics.graphs.launchhistory

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchHistoryInteractorImpl : BaseNetwork(), LaunchHistoryContract.LaunchHistoryInteractor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getLaunches(
        api: SpaceXInterface,
        listener: LaunchHistoryContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel("rocket", populate = "", select = listOf("success_rate_pct"))
        )

        val query = QueryModel(
            QueryUpcomingLaunchesModel(false),
            QueryOptionsModel(false, populateList, "", listOf("rocket", "success"))
        )

        call = api.getQueriedLaunches(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body(), true) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}