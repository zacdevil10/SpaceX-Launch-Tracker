package uk.co.zac_h.spacex.network.retrofit

import retrofit2.Response
import retrofit2.http.POST
import uk.co.zac_h.spacex.network.SPACEX_LANDING_PADS_QUERY
import uk.co.zac_h.spacex.network.SPACEX_LAUNCHPADS_QUERY
import uk.co.zac_h.spacex.network.dto.spacex.LandingPadQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchpadQueriedResponse
import uk.co.zac_h.spacex.network.dto.spacex.NetworkDocsResponse

interface SpaceXService {

    @POST(SPACEX_LANDING_PADS_QUERY)
    suspend fun queryLandingPads(): Response<NetworkDocsResponse<LandingPadQueriedResponse>>

    @POST(SPACEX_LAUNCHPADS_QUERY)
    suspend fun queryLaunchpads(): Response<NetworkDocsResponse<LaunchpadQueriedResponse>>
}
