package uk.co.zac_h.spacex.statistics.graphs.launchmass

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchMassInteractor : BaseNetwork(), LaunchMassContract.Interactor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getLaunches(api: SpaceXInterface, listener: LaunchMassContract.Callback) {
        val query = QueryModel(
            QueryUpcomingSuccessLaunchesModel(upcoming = false, success = true),
            QueryOptionsModel(
                false,
                listOf(
                    QueryPopulateModel(
                        "payloads",
                        populate = "",
                        select = listOf("mass_kg", "orbit")
                    ),
                    QueryPopulateModel("rocket", populate = "", select = listOf("id"))
                ),
                QueryLaunchesSortByDate("asc"),
                listOf("payloads", "name", "date_local", "date_unix", "rocket"),
                1000000
            )
        )

        call = api.getQueriedLaunches(query).apply {
            makeCall {
                onResponseSuccess = { listener.onSuccess(it.body(), true) }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}