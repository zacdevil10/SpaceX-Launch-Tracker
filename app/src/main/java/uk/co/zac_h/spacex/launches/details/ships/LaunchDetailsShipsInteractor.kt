package uk.co.zac_h.spacex.launches.details.ships

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsShipsInteractor : BaseNetwork(), LaunchDetailsShipsContract.Interactor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getShips(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsShipsContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel(
                "ships",
                populate = "",
                select = ""
            )
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(id),
            QueryOptionsModel(false, populateList, "", listOf("ships"), 1)
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