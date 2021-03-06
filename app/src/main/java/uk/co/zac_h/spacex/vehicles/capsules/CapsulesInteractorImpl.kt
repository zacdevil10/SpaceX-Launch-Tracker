package uk.co.zac_h.spacex.vehicles.capsules

import retrofit2.Call
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork
import uk.co.zac_h.spacex.utils.SPACEX_FIELD_CAPSULE_LAUNCHES
import uk.co.zac_h.spacex.utils.SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER
import uk.co.zac_h.spacex.utils.SPACEX_FIELD_LAUNCH_NAME

class CapsulesInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Capsule>?> {

    private var call: Call<CapsulesDocsModel>? = null

    override fun get(
        api: SpaceXInterface,
        listener: NetworkInterface.Callback<List<Capsule>?>
    ) {
        val query = QueryModel(
            "",
            QueryOptionsModel(
                false, listOf(
                    QueryPopulateModel(
                        SPACEX_FIELD_CAPSULE_LAUNCHES,
                        select = listOf(
                            SPACEX_FIELD_LAUNCH_NAME,
                            SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER
                        ),
                        populate = ""
                    )
                ), "", "", 100000
            )
        )

        call = api.queryCapsules(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { Capsule(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}