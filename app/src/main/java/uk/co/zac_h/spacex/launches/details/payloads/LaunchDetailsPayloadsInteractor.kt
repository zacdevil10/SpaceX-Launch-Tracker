package uk.co.zac_h.spacex.launches.details.payloads

import uk.co.zac_h.spacex.base.NetworkInterface
import uk.co.zac_h.spacex.dto.spacex.*
import uk.co.zac_h.spacex.retrofit.SpaceXService
import uk.co.zac_h.spacex.BaseNetwork

class LaunchDetailsPayloadsInteractor : BaseNetwork(), NetworkInterface.Interactor<List<Payload>> {

    //private var call: Call<LaunchDocsModel>? = null

    override fun get(
        data: Any,
        api: SpaceXService,
        listener: NetworkInterface.Callback<List<Payload>>
    ) {
        val populateList = listOf(
            QueryPopulateModel("payloads", populate = "", select = "")
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(data as String),
            QueryOptionsModel(false, populateList, "", listOf("payloads"), 1000)
        )

        /*call = api.queryLaunches(query).apply {
            makeCall {
                onResponseSuccess = { response ->
                    response.body()?.docs?.get(0)
                        ?.let { Launch(it) }
                        ?.payloads
                        ?.let { listener.onSuccess(it) }
                }
                onResponseFailure = { listener.onError(it) }
            }
        }*/
    }

    override fun cancelAllRequests() = terminateAll()
}