package uk.co.zac_h.spacex.statistics.graphs.launchrate

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchRateInteractorImpl : BaseNetwork(), LaunchRateContract.LaunchRateInteractor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getLaunches(
        api: SpaceXInterface,
        listener: LaunchRateContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel("rocket", populate = "", select = listOf("id"))
        )

        val query = QueryModel(
            "",
            QueryOptionsModel(
                false,
                populateList,
                QueryLaunchesSortByDate("asc"),
                listOf("rocket", "success", "upcoming", "date_local"),
                100000
            )
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