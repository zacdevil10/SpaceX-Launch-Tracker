package uk.co.zac_h.spacex.dashboard

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DashboardInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<Launch> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<Launch>
    ) {
        val query = QueryModel(
            query = when (data) {
                is Upcoming -> QueryUpcomingLaunchesModel(data.upcoming)
                else -> QueryLaunchesQueryModel(data as String)
            },
            options = QueryOptionsModel(
                pagination = true,
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
                sort = when (data) {
                    Upcoming.NEXT -> QueryLaunchesSortModel("asc")
                    Upcoming.LATEST -> QueryLaunchesSortModel("desc")
                    else -> ""
                },
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
                    "rocket",
                    "cores",
                    "crew",
                    "ships",
                    "links",
                    "static_fire_date_unix",
                    "details",
                    "launchpad",
                    "date_precision",
                    "upcoming"
                ),
                limit = 1
            )
        )

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)?.let { Launch(it) }?.let {
                        listener.onSuccess(data, it)
                    }
                }
                onResponseFailure = {
                    listener.onError(it)
                }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}