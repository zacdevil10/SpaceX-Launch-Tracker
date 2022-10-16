package uk.co.zac_h.spacex.network.datasource.remote

import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.network.dto.spacex.QueryModel
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import javax.inject.Inject

class LaunchesDataSource @Inject constructor(
    @LaunchLibraryClient private val httpService: LaunchLibraryService
) : RemoteDataSource<LaunchLibraryPaginatedResponse<LaunchResponse>> {

    private suspend fun getLaunches() = httpService.getUpcomingLaunches()

    override suspend fun fetchAsync(query: QueryModel) = getLaunches()
}
