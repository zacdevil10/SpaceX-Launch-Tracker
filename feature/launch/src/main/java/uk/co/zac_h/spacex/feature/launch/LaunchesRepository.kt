package uk.co.zac_h.spacex.feature.launch

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.LaunchesDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.datasource.remote.launches.LaunchesPreviousPagingSource
import uk.co.zac_h.spacex.network.datasource.remote.news.SpaceflightNewsPagingSource
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryClient
import uk.co.zac_h.spacex.network.retrofit.LaunchLibraryService
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsClient
import uk.co.zac_h.spacex.network.retrofit.SpaceflightNewsService
import javax.inject.Inject

class LaunchesRepository @Inject constructor(
    @LaunchesDataSourceClient launchesDataSource: RemoteDataSource<LaunchLibraryPaginatedResponse<LaunchResponse>>,
    cache: Cache<LaunchLibraryPaginatedResponse<LaunchResponse>>,
    @LaunchLibraryClient private val httpService: LaunchLibraryService,
    @SpaceflightNewsClient private val newsHttpService: SpaceflightNewsService,
) : Repository<LaunchLibraryPaginatedResponse<LaunchResponse>>(launchesDataSource, cache) {

    val previousLaunchesPagingSource: LaunchesPreviousPagingSource
        get() = LaunchesPreviousPagingSource(httpService)

    fun articlesPagingSource(launchId: String?) = SpaceflightNewsPagingSource(
        httpService = newsHttpService,
        launch = launchId
    )
}
