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
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto

/**
 * Launch Library 2.3.0. Each endpoint asks for one `mode`, and the return type is the generated
 * model for that mode (see network/openapi). Changing a `mode` means changing the return type.
 */
interface LaunchLibraryService {

    @GET(SPACEX_UPCOMING_LAUNCHES)
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int = 10,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed",
        // Comma-separated status ids to filter by, or null for all.
        @Query("status__ids") statusIds: String? = null
    ): LaunchLibraryPaginatedResponse<LaunchDetailedDto>

    @GET(SPACEX_PREVIOUS_LAUNCHES)
    suspend fun getPreviousLaunches(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LaunchDetailedDto>

    @GET(SPACEX_LAUNCHER)
    suspend fun getLaunchers(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("launcher_config__manufacturer__name") launcherConfigManufacturerName: String = "SpaceX",
        @Query("ordering") ordering: String = "-id",
        @Query("is_placeholder") isPlaceholder: Boolean = false,
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LauncherDetailedDto>

    @GET(SPACEX_LAUNCHER)
    suspend fun getLaunchersForConfig(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("launcher_config__manufacturer__name") launcherConfigManufacturerName: String = "SpaceX",
        @Query("ordering") ordering: String = "-id",
        @Query("is_placeholder") isPlaceholder: Boolean = false,
        @Query("launcher_config__ids") launcherConfigId: Int,
        @Query("mode") mode: String = "detailed"
    ): LaunchLibraryPaginatedResponse<LauncherDetailedDto>

    @GET(SPACEX_SPACECRAFT)
    suspend fun getSpacecrafts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "Dragon",
        @Query("ordering") ordering: String = "-id",
        @Query("mode") mode: String = "normal"
    ): LaunchLibraryPaginatedResponse<SpacecraftNormalDto>

    @GET(SPACEX_ASTRONAUTS)
    suspend fun getAstronauts(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("search") search: String = "SpaceX",
        @Query("mode") mode: String = "normal"
    ): LaunchLibraryPaginatedResponse<AstronautEndpointNormalDto>

    @GET(SPACEX_AGENCY)
    suspend fun getAgency(): AgencyEndpointDetailedDto
}
