package uk.co.zac_h.spacex.network.retrofit

import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.network.SPACEX_AGENCY
import uk.co.zac_h.spacex.network.SPACEX_ASTRONAUTS
import uk.co.zac_h.spacex.network.SPACEX_LAUNCHER
import uk.co.zac_h.spacex.network.SPACEX_PREVIOUS_LAUNCHES
import uk.co.zac_h.spacex.network.SPACEX_SPACECRAFT
import uk.co.zac_h.spacex.network.SPACEX_UPCOMING_LAUNCHES
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailed
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormal
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailed
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailed
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormal

/**
 * Launch Library 2.3.0. Each endpoint asks for one `mode`, and the return type is the generated
 * model for that mode (see network/openapi). Changing a `mode` means changing the return type.
 */
interface LaunchLibraryService {

    @GET(SPACEX_UPCOMING_LAUNCHES)
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 10,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LaunchDetailed>

    @GET(SPACEX_PREVIOUS_LAUNCHES)
    suspend fun getPreviousLaunches(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LaunchDetailed>

    @GET(SPACEX_LAUNCHER)
    suspend fun getLaunchers(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("launcher_config__manufacturer__name") launcherConfigManufacturerName: String = "SpaceX",
        @Query("ordering") ordering: String = "-id",
        @Query("is_placeholder") isPlaceholder: Boolean = false,
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LauncherDetailed>

    @GET(SPACEX_LAUNCHER)
    suspend fun getLaunchersForConfig(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("launcher_config__manufacturer__name") launcherConfigManufacturerName: String = "SpaceX",
        @Query("ordering") ordering: String = "-id",
        @Query("is_placeholder") isPlaceholder: Boolean = false,
        @Query("launcher_config__ids") launcherConfigId: Int,
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LauncherDetailed>

    @GET(SPACEX_SPACECRAFT)
    suspend fun getSpacecrafts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "Dragon",
        @Query("ordering") ordering: String = "-id",
        @Query("mode") mode: String = "normal"
    ): LaunchLibraryPaginatedResponse<SpacecraftNormal>

    @GET(SPACEX_ASTRONAUTS)
    suspend fun getAstronauts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "normal"
    ): LaunchLibraryPaginatedResponse<AstronautEndpointNormal>

    @GET(SPACEX_AGENCY)
    suspend fun getAgency(): AgencyEndpointDetailed
}
