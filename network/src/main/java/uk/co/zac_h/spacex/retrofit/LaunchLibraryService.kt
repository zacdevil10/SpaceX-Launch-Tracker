package uk.co.zac_h.spacex.retrofit

import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import uk.co.zac_h.spacex.SPACEX_PREVIOUS_LAUNCHES
import uk.co.zac_h.spacex.SPACEX_UPCOMING_LAUNCHES
import uk.co.zac_h.spacex.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.dto.spacex.LaunchResponse

interface LaunchLibraryService {

    @GET(SPACEX_UPCOMING_LAUNCHES)
    suspend fun getUpcomingLaunches(
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
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
}