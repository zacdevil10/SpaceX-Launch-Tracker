package uk.co.zac_h.spacex.network.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.network.SPACEX_AGENCY
import uk.co.zac_h.spacex.network.SPACEX_ASTRONAUTS
import uk.co.zac_h.spacex.network.SPACEX_LAUNCHER
import uk.co.zac_h.spacex.network.SPACEX_PREVIOUS_LAUNCHES
import uk.co.zac_h.spacex.network.SPACEX_SPACECRAFT
import uk.co.zac_h.spacex.network.SPACEX_UPCOMING_LAUNCHES
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import uk.co.zac_h.spacex.network.dto.spacex.AstronautResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import uk.co.zac_h.spacex.network.dto.spacex.SpacecraftResponse

interface LaunchLibraryService {

    @GET(SPACEX_UPCOMING_LAUNCHES)
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 10,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed"
    ): Response<LaunchLibraryPaginatedResponse<LaunchResponse>>

    @GET(SPACEX_PREVIOUS_LAUNCHES)
    suspend fun getPreviousLaunches(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed"
    ): Response<LaunchLibraryPaginatedResponse<LaunchResponse>>

    @GET(SPACEX_LAUNCHER)
    suspend fun getLaunchers(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("launcher_config__manufacturer__name") launcherConfigManufacturerName: String = "SpaceX",
        @Query("ordering") mode: String = "-id",
        @Query("is_placeholder") isPlaceholder: Boolean = false
    ): Response<LaunchLibraryPaginatedResponse<LauncherResponse>>

    @GET(SPACEX_LAUNCHER)
    suspend fun getLaunchersForConfig(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("launcher_config__manufacturer__name") launcherConfigManufacturerName: String = "SpaceX",
        @Query("ordering") mode: String = "-id",
        @Query("is_placeholder") isPlaceholder: Boolean = false,
        @Query("launcher_config__ids") launcherConfigId: Int
    ): Response<LaunchLibraryPaginatedResponse<LauncherResponse>>

    @GET(SPACEX_SPACECRAFT)
    suspend fun getSpacecrafts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "Dragon",
        @Query("ordering") mode: String = "-id"
    ): Response<LaunchLibraryPaginatedResponse<SpacecraftResponse>>

    @GET(SPACEX_ASTRONAUTS)
    suspend fun getAstronauts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed"
    ): Response<LaunchLibraryPaginatedResponse<AstronautResponse>>

    @GET(SPACEX_AGENCY)
    suspend fun getAgency(): Response<AgencyResponse>
}