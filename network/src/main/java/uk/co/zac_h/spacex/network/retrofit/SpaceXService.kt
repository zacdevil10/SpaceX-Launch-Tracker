package uk.co.zac_h.spacex.network.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import uk.co.zac_h.spacex.network.*
import uk.co.zac_h.spacex.network.dto.spacex.*

interface SpaceXService {

    @POST(SPACEX_CAPSULES_QUERY)
    suspend fun queryCapsules(@Body body: QueryModel): Response<NetworkDocsResponse<CapsuleQueriedResponse>>

    @GET(SPACEX_COMPANY)
    suspend fun getCompanyInfo(): Response<CompanyResponse>

    @POST(SPACEX_CORES_QUERY)
    suspend fun queryCores(@Body body: QueryModel): Response<NetworkDocsResponse<CoreQueriedResponse>>

    @POST(SPACEX_CREW_QUERY)
    suspend fun queryCrewMembers(@Body body: QueryModel): Response<NetworkDocsResponse<CrewQueriedResponse>>

    @GET(SPACEX_DRAGONS)
    suspend fun getDragons(): Response<MutableList<DragonResponse>>

    @POST(SPACEX_LANDING_PADS_QUERY)
    suspend fun queryLandingPads(@Body body: QueryModel): Response<NetworkDocsResponse<LandingPadQueriedResponse>>

    @POST(SPACEX_LAUNCHPADS_QUERY)
    suspend fun queryLaunchpads(@Body body: QueryModel): Response<NetworkDocsResponse<LaunchpadQueriedResponse>>

    @GET(SPACEX_ROCKETS)
    suspend fun getRockets(): Response<MutableList<RocketResponse>>

    @POST(SPACEX_SHIPS_QUERY)
    suspend fun queryShips(@Body body: QueryModel): Response<NetworkDocsResponse<ShipQueriedResponse>>

    @POST(SPACEX_HISTORY_QUERY)
    suspend fun queryHistory(@Body body: QueryModel): Response<NetworkDocsResponse<HistoryResponse>>
}
