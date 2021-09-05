package uk.co.zac_h.spacex.retrofit

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.dto.spacex.*

interface SpaceXService {

    @GET(SPACEX_CAPSULES)
    suspend fun getCapsules(): Response<List<CapsuleResponse>>

    @GET(SPACEX_CAPSULES + SPACEX_GET_BY_PARAM_ID)
    suspend fun getCapsule(@Path(SPACEX_PARAM_ID) id: String): Response<CapsuleResponse>

    @POST(SPACEX_CAPSULES_QUERY)
    suspend fun queryCapsules(@Body body: QueryModel): Response<CapsulesDocsModel>

    @GET(SPACEX_COMPANY)
    suspend fun getCompanyInfo(): Response<CompanyResponse>

    @GET(SPACEX_CORES)
    suspend fun getCores(): Response<List<CoreResponse>>

    @GET(SPACEX_CORES + SPACEX_GET_BY_PARAM_ID)
    suspend fun getCore(@Path(SPACEX_PARAM_ID) id: String): Response<CoreResponse>

    @POST(SPACEX_CORES_QUERY)
    suspend fun queryCores(@Body body: QueryModel): Response<CoreDocsModel>

    @GET(SPACEX_CREW)
    suspend fun getCrewMembers(): Response<List<CrewResponse>>

    @GET(SPACEX_CREW + SPACEX_GET_BY_PARAM_ID)
    suspend fun getCrewMember(@Path(SPACEX_PARAM_ID) id: String): Response<CrewResponse>

    @POST(SPACEX_CREW_QUERY)
    suspend fun queryCrewMembers(@Body body: QueryModel): Response<CrewDocsModel>

    @GET(SPACEX_DRAGONS)
    suspend fun getDragons(): Response<MutableList<DragonResponse>>

    @GET(SPACEX_DRAGONS + SPACEX_GET_BY_PARAM_ID)
    suspend fun getDragon(@Path(SPACEX_PARAM_ID) id: String): Response<DragonResponse>

    @GET(SPACEX_DRAGONS_QUERY)
    suspend fun queryDragons(@Body body: QueryModel): Response<DragonDocsModel>

    @GET(SPACEX_LANDING_PADS)
    suspend fun getLandingPads(): Response<List<LandingPadResponse>>

    @GET(SPACEX_LANDING_PADS + SPACEX_GET_BY_PARAM_ID)
    suspend fun getLandingPad(@Path(SPACEX_PARAM_ID) id: String): Response<LandingPadResponse>

    @POST(SPACEX_LANDING_PADS_QUERY)
    suspend fun queryLandingPads(@Body body: QueryModel): Response<LandingPadDocsModel>

    @GET(SPACEX_LAUNCHES_PAST)
    suspend fun getLaunchesPast(): Response<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES_UPCOMING)
    suspend fun getLaunchesUpcoming(): Response<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES_LATEST)
    suspend fun getLaunchesLatest(): Response<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES_NEXT)
    suspend fun getLaunchesNext(): Response<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES)
    suspend fun getLaunches(): Response<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES + SPACEX_GET_BY_PARAM_ID)
    suspend fun getLaunch(@Path(SPACEX_PARAM_ID) id: String): Response<LaunchResponse>

    @POST(SPACEX_LAUNCHES_QUERY)
    suspend fun queryLaunches(@Body body: QueryModel): Response<LaunchDocsModel>

    @GET(SPACEX_LAUNCHPADS)
    suspend fun getLaunchpads(): Response<List<LaunchpadResponse>>

    @GET(SPACEX_LAUNCHPADS + SPACEX_GET_BY_PARAM_ID)
    suspend fun getLaunchpad(@Path(SPACEX_PARAM_ID) id: String): Response<LaunchpadResponse>

    @POST(SPACEX_LAUNCHPADS_QUERY)
    suspend fun queryLaunchpads(@Body body: QueryModel): Response<LaunchpadDocsModel>

    @GET(SPACEX_PAYLOADS)
    suspend fun getPayloads(): Response<List<PayloadResponse>>

    @GET(SPACEX_PAYLOADS + SPACEX_GET_BY_PARAM_ID)
    suspend fun getPayload(): Response<PayloadResponse>

    @POST(SPACEX_PAYLOADS_QUERY)
    suspend fun queryPayloads(@Body body: QueryModel): Response<PayloadDocsModel>

    @GET(SPACEX_ROADSTER)
    suspend fun getRoadsterInfo(): Response<RoadsterResponse>

    @POST(SPACEX_ROADSTER_QUERY)
    suspend fun queryRoadsterInfo(): Response<RoadsterDocsModel>

    @GET(SPACEX_ROCKETS)
    suspend fun getRockets(): Response<MutableList<RocketResponse>>

    @GET(SPACEX_ROCKETS + SPACEX_GET_BY_PARAM_ID)
    suspend fun getRocket(@Path(SPACEX_PARAM_ID) id: String): Response<RocketResponse>

    @GET(SPACEX_ROCKETS_QUERY)
    suspend fun queryRockets(@Body body: QueryModel): Response<RocketDocsModel>

    @GET(SPACEX_SHIPS)
    suspend fun getShips(): Response<List<ShipResponse>>

    @GET(SPACEX_SHIPS + SPACEX_GET_BY_PARAM_ID)
    suspend fun getShip(@Path(SPACEX_PARAM_ID) id: String): Response<ShipResponse>

    @POST(SPACEX_SHIPS_QUERY)
    suspend fun queryShips(@Body body: QueryModel): Response<ShipsDocsModel>

    @GET(SPACEX_STARLINK)
    suspend fun getStarlinkSatellites(): Response<Any>

    @GET(SPACEX_STARLINK + SPACEX_GET_BY_PARAM_ID)
    suspend fun getStarlinkSatellite(@Path(SPACEX_PARAM_ID) id: String): Response<Any>

    @POST(SPACEX_STARLINK_QUERY)
    suspend fun queryStarlinkSatellites(@Body body: QueryModel): Response<Any>

    @GET(SPACEX_HISTORY)
    suspend fun getHistory(): Response<List<HistoryResponse>>

    @GET(SPACEX_HISTORY + SPACEX_GET_BY_PARAM_ID)
    suspend fun getHistoricalEvent(): Response<HistoryResponse>

    @POST(SPACEX_HISTORY_QUERY)
    suspend fun queryHistory(@Body body: QueryModel): Response<HistoryDocsModel>

}