package uk.co.zac_h.spacex.network.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.network.datasource.remote.about.AgencyDataSource
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    @AgencyDataSourceClient
    abstract fun bindAgencyDataSource(agencyDataSource: AgencyDataSource): RemoteDataSource<AgencyResponse>

    @Binds
    @LaunchesDataSourceClient
    abstract fun bindLaunchesDataSource(launchesDataSource: LaunchesDataSource): RemoteDataSource<LaunchLibraryPaginatedResponse<LaunchResponse>>
}

@Qualifier
annotation class AgencyDataSourceClient

@Qualifier
annotation class LaunchesDataSourceClient
