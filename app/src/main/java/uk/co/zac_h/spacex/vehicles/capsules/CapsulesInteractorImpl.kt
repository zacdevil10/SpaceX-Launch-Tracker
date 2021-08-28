package uk.co.zac_h.spacex.vehicles.capsules

import uk.co.zac_h.spacex.SPACEX_FIELD_CAPSULE_LAUNCHES
import uk.co.zac_h.spacex.SPACEX_FIELD_LAUNCH_FLIGHT_NUMBER
import uk.co.zac_h.spacex.SPACEX_FIELD_LAUNCH_NAME
import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.Capsule
import uk.co.zac_h.spacex.dto.spacex.QueryModel
import uk.co.zac_h.spacex.dto.spacex.QueryOptionsModel
import uk.co.zac_h.spacex.dto.spacex.QueryPopulateModel
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class CapsulesInteractorImpl : BaseNetwork(), NetworkInterface.Interactor<List<Capsule>?> {

    //private var call: Call<CapsulesDocsModel>? = null

    override fun get(
        api: SpaceXService,
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

        /*call = api.queryCapsules(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onSuccess(response.body()?.docs?.map { Capsule(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}