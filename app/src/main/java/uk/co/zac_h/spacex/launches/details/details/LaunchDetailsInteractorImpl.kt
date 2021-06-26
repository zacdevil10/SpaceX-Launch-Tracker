package uk.co.zac_h.spacex.launches.details.details

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<Launch> {

    private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<Launch>
    ) {
        val populateList = listOf(
            QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
            QueryPopulateModel("rocket", populate = "", select = listOf("name"))
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(data as String),
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
                    "date_precision",
                    "upcoming"
                ),
                limit = 1
            )
        )

        call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)
                        ?.let { Launch(it) }
                        ?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}