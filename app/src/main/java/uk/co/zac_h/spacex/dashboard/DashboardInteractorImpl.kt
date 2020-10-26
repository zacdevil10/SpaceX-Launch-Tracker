package uk.co.zac_h.spacex.dashboard

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class DashboardInteractorImpl : BaseNetwork(), DashboardContract.DashboardInteractor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: DashboardContract.InteractorCallback
    ) {
        val query = QueryModel(
            query = when (id) {
                "next", "latest" -> QueryUpcomingLaunchesModel(id == "next")
                else -> QueryLaunchesQueryModel(id)
            },
            options = QueryOptionsModel(
                pagination = true,
                populate = listOf(
                    QueryPopulateModel(path = "rocket", populate = "", select = listOf("name")),
                    QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
                    QueryPopulateModel("crew", populate = "", select = listOf("id")),
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
                sort = when (id) {
                    "next" -> QueryLaunchesSortModel("asc")
                    "latest" -> QueryLaunchesSortModel("desc")
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
                    "date_precision"
                ),
                limit = 1
            )
        )

        call = api.getQueriedLaunches(query).apply {
            makeCall {
                onResponseSuccess = {
                    listener.onSuccess(id, it.body()?.docs?.get(0))
                }
                onResponseFailure = {
                    listener.onError(it)
                }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}