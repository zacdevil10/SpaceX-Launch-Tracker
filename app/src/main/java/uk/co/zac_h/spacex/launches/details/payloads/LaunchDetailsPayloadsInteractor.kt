package uk.co.zac_h.spacex.launches.details.payloads

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class LaunchDetailsPayloadsInteractor : BaseNetwork(), LaunchDetailsPayloadsContract.Interactor {

    private var call: Call<LaunchesExtendedDocsModel>? = null

    override fun getPayloads(
        id: String,
        api: SpaceXInterface,
        listener: LaunchDetailsPayloadsContract.InteractorCallback
    ) {
        val populateList = listOf(
            QueryPopulateModel("payloads", populate = "", select = "")
        )

        val query = QueryModel(
            QueryLaunchesQueryModel(id),
            QueryOptionsModel(false, populateList, "", listOf("payloads"), 1000)
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