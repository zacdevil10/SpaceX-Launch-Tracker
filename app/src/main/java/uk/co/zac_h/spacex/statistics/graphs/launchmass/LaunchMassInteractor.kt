package uk.co.zac_h.spacex.statistics.graphs.launchmass

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchMassInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Launch>?> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        api: SpaceXService,
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