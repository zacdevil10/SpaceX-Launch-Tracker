package uk.co.zac_h.spacex.launches

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchesInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Launch>> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Launch>>
    ) {
        val query = QueryModel(
            query = QueryUpcomingLaunchesModel(data == "upcoming"),
            options = QueryOptionsModel(
                pagination = false,
                populate = listOf(
                    QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                    QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                    QueryPopulateModel("crew.crew", populate = "", select = listOf("id")),
                    QueryPopulateModel("ships", populate = "", select = listOf("id")),
                    QueryPopulateModel(
                        path = "cores",
                        populate = listOf(
                            QueryPopulateModel(
                                path = "landpad",
                                populate = "",
                                select = listOf("name")
                            ),
                            QueryPopulateModel(
                                path = "core",
                                populate = "",
                                select = listOf("reuse_count")
                            )
                        ),
                        select = ""
                    )
                ),
                sort = QueryLaunchesSortModel(if (data == "past") "desc" else "asc"),
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
                    "upcoming",
                    "rocket",
                    "cores",
                    "crew",
                    "ships",
                    "links",
                    "static_fire_date_unix",
                    "details",
                    "launchpad",
                    "date_precision"
                ),
                limit = 5000
            )
        )

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.map { Launch(it) }?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}