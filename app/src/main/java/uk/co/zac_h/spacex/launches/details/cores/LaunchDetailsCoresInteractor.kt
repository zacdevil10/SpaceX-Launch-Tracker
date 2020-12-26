package uk.co.zac_h.spacex.launches.details.cores

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsCoresInteractor : BaseNetwork(), LaunchDetailsCoresContract.Interactor {

    private var call: Call<LaunchDocsModel>? = null

    override fun getCores(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsCoresContract.InteractorCallback
    ) {
        val populateList = listOf(
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
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                select = listOf("id"),
                                populate = ""
                            )
                        )
                    )
                ),
                select = ""
            )
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(id),
            QueryOptionsModel(false, populateList, "", listOf("cores"), 10)
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