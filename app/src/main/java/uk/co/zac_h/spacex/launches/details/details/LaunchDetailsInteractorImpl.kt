package uk.co.zac_h.spacex.launches.details.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsInteractorImpl : BaseNetwork(), LaunchDetailsContract.LaunchDetailsInteractor {

    private var call: Call<LaunchDocsModel>? = null

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

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.get(0)?.let { Launch(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelRequest() = terminateAll()
}