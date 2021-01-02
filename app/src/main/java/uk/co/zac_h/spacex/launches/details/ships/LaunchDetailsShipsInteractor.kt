package uk.co.zac_h.spacex.launches.details.ships

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsShipsInteractor : BaseNetwork(), LaunchDetailsShipsContract.Interactor {

    private var call: Call<LaunchDocsModel>? = null

    override fun getShips(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsShipsContract.InteractorCallback
    ) {
        val query = QueryModel(
            QueryLaunchesQueryModel(id),
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "ships",
                        populate = listOf(
                            QueryPopulateModel(
                                "launches",
                                populate = "",
                                select = listOf("name", "flight_number")
                            )
                        ),
                        select = ""
                    )
                ), "", listOf("ships"), 1
            )
        )

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.get(0)?.let { Launch(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelRequest() = terminateAll()
}