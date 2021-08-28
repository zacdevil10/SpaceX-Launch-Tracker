package uk.co.zac_h.spacex.statistics.graphs.launchrate

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchRateInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Launch>?>
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
                listOf("rocket", "success", "upcoming", "date_unix"),
                100000
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