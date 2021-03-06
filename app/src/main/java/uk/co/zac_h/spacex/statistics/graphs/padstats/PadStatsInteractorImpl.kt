package uk.co.zac_h.spacex.statistics.graphs.padstats

import retrofit2.Call
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.rest.SpaceXInterface
import uk.co.zac_h.spacex.utils.BaseNetwork

class PadStatsInteractorImpl : BaseNetwork(), PadStatsContract.PadStatsInteractor {

    private var launchCall: Call<LaunchpadDocsModel>? = null
    private var landingCall: Call<LandingPadDocsModel>? = null

    override fun getLaunchpads(
        api: SpaceXInterface,
        listener: PadStatsContract.InteractorCallback
    ) {
        val launchQuery = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                "",
                listOf("full_name", "launch_attempts", "launch_successes", "status"),
                100000
            )
        )

        launchCall = api.queryLaunchpads(launchQuery).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onGetLaunchpads(response.body()?.docs?.map { Launchpad(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun getLandingPads(
        api: SpaceXInterface,
        listener: PadStatsContract.InteractorCallback
    ) {
        val landQuery = QueryModel(
            "",
            QueryOptionsModel(
                false,
                "",
                "",
                listOf("full_name", "landing_attempts", "landing_successes", "status", "type"),
                100000
            )
        )

        landingCall = api.queryLandingPads(landQuery).apply {
            makeCall {
                onResponseSuccess = { response ->
                    listener.onGetLandingPads(response.body()?.docs?.map { LandingPad(it) })
                }
                onResponseFailure = { listener.onError(it) }
            }
        }
    }

    override fun cancelAllRequests() = terminateAll()
}