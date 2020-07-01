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
        val populateList = listOf(
            QueryPopulateModel("rocket", populate = "", select = listOf("name"))
        )

        val query = QueryModel(
            when (id) {
                "next", "latest" -> QueryUpcomingLaunchesModel(id == "next")
                else -> QueryLaunchesQueryModel(id)
            },
            QueryOptionsModel(
                true,
                populateList,
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
                    "links.patch.small",
                    "rocket"
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