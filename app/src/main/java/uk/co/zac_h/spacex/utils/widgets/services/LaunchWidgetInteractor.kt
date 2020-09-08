package uk.co.zac_h.spacex.utils.widgets.services

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchWidgetInteractor : BaseNetwork(), LaunchWidgetContract.Interactor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getLaunch(id: Int, api: SpaceXInterface, listener: LaunchWidgetContract.Callback) {
        val query = QueryModel(
            query = QueryUpcomingLaunchesModel(true),
            options = QueryOptionsModel(
                pagination = true,
                populate = "",
                sort = QueryLaunchesSortModel("asc"),
                select = listOf(
                    "flight_number",
                    "name",
                    "date_unix",
                    "tbd",
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

}