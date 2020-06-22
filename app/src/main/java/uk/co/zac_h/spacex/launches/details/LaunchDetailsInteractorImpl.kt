package uk.co.zac_h.spacex.launches.details

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsInteractorImpl : BaseNetwork(), LaunchDetailsContract.LaunchDetailsInteractor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getSingleLaunch(
        flightNumber: Int,
        api: SpaceXInterface,
        listener: LaunchDetailsContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel("launchpad", select = listOf("name"), populate = ""),
            QueryPopulateModel("payloads", populate = "", select = ""),
            QueryPopulateModel("rocket", populate = "", select = listOf("name")),
            QueryPopulateModel(
                "cores",
                populate = listOf(
                    QueryPopulateModel(
                        "core",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                select = listOf("name", "flight_number"),
                                populate = ""
                            )
                        ),
                        select = ""
                    ),
                    QueryPopulateModel(
                        "landpad",
                        select = "",
                        populate = ""
                    )
                ),
                select = ""
            )
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(flightNumber),
            QueryOptionsModel(false, populateList, "", "")
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