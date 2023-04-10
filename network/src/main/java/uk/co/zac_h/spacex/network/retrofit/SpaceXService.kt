package uk.co.zac_h.spacex.network.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.spacex.*

interface SpaceXService {

    @POST(SPACEX_CAPSULES_QUERY)
    suspend fun queryCapsules(@Body body: QueryModel): Response<NetworkDocsResponse<CapsuleQueriedResponse>>

    @POST(SPACEX_LANDING_PADS_QUERY)
    suspend fun queryLandingPads(@Body body: QueryModel): Response<NetworkDocsResponse<LandingPadQueriedResponse>>

    @POST(SPACEX_LAUNCHPADS_QUERY)
    suspend fun queryLaunchpads(@Body body: QueryModel): Response<NetworkDocsResponse<LaunchpadQueriedResponse>>
}
