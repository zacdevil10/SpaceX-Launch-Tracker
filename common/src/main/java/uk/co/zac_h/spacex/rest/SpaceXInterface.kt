package uk.co.zac_h.spacex.rest

import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import uk.co.zac_h.spacex.model.spacex.*
import uk.co.zac_h.spacex.utils.*

interface SpaceXInterface {

    @GET(SPACEX_CAPSULES)
    fun getCapsules(): Call<List<CapsuleResponse>>

    @GET(SPACEX_CAPSULES + SPACEX_GET_BY_PARAM_ID)
    fun getCapsule(@Path(SPACEX_PARAM_ID) id: String): Call<CapsuleResponse>

    @POST(SPACEX_CAPSULES_QUERY)
    fun queryCapsules(@Body body: QueryModel): Call<CapsulesDocsModel>

    @GET(SPACEX_COMPANY)
    fun getCompanyInfo(): Call<CompanyResponse>

    @GET(SPACEX_CORES)
    fun getCores(): Call<List<CoreResponse>>

    @GET(SPACEX_CORES + SPACEX_GET_BY_PARAM_ID)
    fun getCore(@Path(SPACEX_PARAM_ID) id: String): Call<CoreResponse>

    @POST(SPACEX_CORES_QUERY)
    fun queryCores(@Body body: QueryModel): Call<CoreDocsModel>

    @GET(SPACEX_CREW)
    fun getCrewMembers(): Call<List<CrewResponse>>

    @GET(SPACEX_CREW + SPACEX_GET_BY_PARAM_ID)
    fun getCrewMember(@Path(SPACEX_PARAM_ID) id: String): Call<CrewResponse>

    @POST(SPACEX_CREW_QUERY)
    fun queryCrewMembers(@Body body: QueryModel): Call<CrewDocsModel>

    @GET(SPACEX_DRAGONS)
    fun getDragons(): Call<List<DragonResponse>>

    @GET(SPACEX_DRAGONS + SPACEX_GET_BY_PARAM_ID)
    fun getDragon(@Path(SPACEX_PARAM_ID) id: String): Call<DragonResponse>

    @GET(SPACEX_DRAGONS_QUERY)
    fun queryDragons(@Body body: QueryModel): Call<DragonDocsModel>

    @GET(SPACEX_LANDING_PADS)
    fun getLandingPads(): Call<List<LandingPadResponse>>

    @GET(SPACEX_LANDING_PADS + SPACEX_GET_BY_PARAM_ID)
    fun getLandingPad(@Path(SPACEX_PARAM_ID) id: String): Call<LandingPadResponse>

    @POST(SPACEX_LANDING_PADS_QUERY)
    fun queryLandingPads(@Body body: QueryModel): Call<LandingPadDocsModel>

    @GET(SPACEX_LAUNCHES_PAST)
    fun getLaunchesPast(): Call<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES_UPCOMING)
    fun getLaunchesUpcoming(): Call<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES_LATEST)
    fun getLaunchesLatest(): Call<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES_NEXT)
    fun getLaunchesNext(): Call<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES)
    fun getLaunches(): Call<List<LaunchResponse>>

    @GET(SPACEX_LAUNCHES + SPACEX_GET_BY_PARAM_ID)
    fun getLaunch(@Path(SPACEX_PARAM_ID) id: String): Call<LaunchResponse>

    @POST(SPACEX_LAUNCHES_QUERY)
    fun queryLaunches(@Body body: QueryModel): Call<LaunchDocsModel>

    @GET(SPACEX_LAUNCHPADS)
    fun getLaunchpads(): Call<List<LaunchpadResponse>>

    @GET(SPACEX_LAUNCHPADS + SPACEX_GET_BY_PARAM_ID)
    fun getLaunchpad(@Path(SPACEX_PARAM_ID) id: String): Call<LaunchpadResponse>

    @POST(SPACEX_LAUNCHPADS_QUERY)
    fun queryLaunchpads(@Body body: QueryModel): Call<LaunchpadDocsModel>

    @GET(SPACEX_PAYLOADS)
    fun getPayloads(): Call<List<PayloadResponse>>

    @GET(SPACEX_PAYLOADS + SPACEX_GET_BY_PARAM_ID)
    fun getPayload(): Call<PayloadResponse>

    @POST(SPACEX_PAYLOADS_QUERY)
    fun queryPayloads(@Body body: QueryModel): Call<PayloadDocsModel>

    @GET(SPACEX_ROADSTER)
    fun getRoadsterInfo(): Call<RoadsterResponse>

    @POST(SPACEX_ROADSTER_QUERY)
    fun queryRoadsterInfo(): Call<RoadsterDocsModel>

    @GET(SPACEX_ROCKETS)
    fun getRockets(): Call<List<RocketResponse>>

    @GET(SPACEX_ROCKETS + SPACEX_GET_BY_PARAM_ID)
    fun getRocket(@Path(SPACEX_PARAM_ID) id: String): Call<RocketResponse>

    @GET(SPACEX_ROCKETS_QUERY)
    fun queryRockets(@Body body: QueryModel): Call<RocketDocsModel>

    @GET(SPACEX_SHIPS)
    fun getShips(): Call<List<ShipResponse>>

    @GET(SPACEX_SHIPS + SPACEX_GET_BY_PARAM_ID)
    fun getShip(@Path(SPACEX_PARAM_ID) id: String): Call<ShipResponse>

    @POST(SPACEX_SHIPS_QUERY)
    fun queryShips(@Body body: QueryModel): Call<ShipsDocsModel>

    @GET(SPACEX_STARLINK)
    fun getStarlinkSatellites(): Call<Any>

    @GET(SPACEX_STARLINK + SPACEX_GET_BY_PARAM_ID)
    fun getStarlinkSatellite(@Path(SPACEX_PARAM_ID) id: String): Call<Any>

    @POST(SPACEX_STARLINK_QUERY)
    fun queryStarlinkSatellites(@Body body: QueryModel): Call<Any>

    @GET(SPACEX_HISTORY)
    fun getHistory(): Call<List<HistoryResponse>>

    @GET(SPACEX_HISTORY + SPACEX_GET_BY_PARAM_ID)
    fun getHistoricalEvent(): Call<HistoryResponse>

    @POST(SPACEX_HISTORY_QUERY)
    fun queryHistory(@Body body: QueryModel): Call<HistoryDocsModel>

    companion object RetrofitSetup {
        fun create(baseUrl: String = SPACEX_BASE_URL_V4): SpaceXInterface = Retrofit.Builder().apply {
            baseUrl(baseUrl)
            addConverterFactory(MoshiConverterFactory.create())
        }.build().create(SpaceXInterface::class.java)
    }
}