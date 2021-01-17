package uk.co.zac_h.spacex.statistics.graphs.launchmass

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchMassInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Launch>?>
    ) {
        val query = QueryModel(
            QueryUpcomingSuccessLaunchesModel(upcoming = false, success = true),
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "payloads",
                        populate = "",
                        select = listOf("mass_kg", "orbit")
                    ),
                    QueryPopulateModel("rocket", populate = "", select = listOf("id"))
                ),
                QueryLaunchesSortByDate("asc"),
                listOf("payloads", "name", "date_unix", "rocket"),
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