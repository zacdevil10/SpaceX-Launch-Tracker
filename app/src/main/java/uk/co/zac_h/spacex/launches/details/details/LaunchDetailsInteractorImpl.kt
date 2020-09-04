package uk.co.zac_h.spacex.launches.details.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsInteractorImpl : BaseNetwork(), LaunchDetailsContract.LaunchDetailsInteractor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getSingleLaunch(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
            QueryPopulateModel("rocket", populate = "", select = listOf("name"))
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(id),
            QueryOptionsModel(
                true,
                populate = populateList,
                sort = "",
                select = listOf(
                    "links",
                    "static_fire_date_unix",
                    "tbd",
                    "net",
                    "rocket",
                    "details",
                    "launchpad",
                    "flight_number",
                    "name",
                    "date_unix",
                    "date_precision"
                ),
                limit = 1
            )
        )

        call = api.getQueriedLaunches(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body()) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelRequest() = terminateAll()
}