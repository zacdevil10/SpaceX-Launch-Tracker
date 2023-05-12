package uk.co.zac_h.spacex.network.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.network.datasource.remote.about.AgencyDataSource
import uk.co.zac_h.spacex.network.datasource.remote.statistics.LandingPadDataSource
import uk.co.zac_h.spacex.network.datasource.remote.statistics.LaunchpadDataSource
import uk.co.zac_h.spacex.network.datasource.remote.vehicles.*
import uk.co.zac_h.spacex.network.dto.spacex.*
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

    @Binds
    @LaunchpadDataSourceClient
    abstract fun bindLaunchpadDataSource(launchpadDataSource: LaunchpadDataSource): RemoteDataSource<NetworkDocsResponse<LaunchpadQueriedResponse>>

    @Binds
    @LandingPadDataSourceClient
    abstract fun bindLandingPadDataSource(landingPadDataSource: LandingPadDataSource): RemoteDataSource<NetworkDocsResponse<LandingPadQueriedResponse>>
}

@Qualifier
annotation class AgencyDataSourceClient

@Qualifier
annotation class LaunchesDataSourceClient

@Qualifier
annotation class LaunchpadDataSourceClient

@Qualifier
annotation class LandingPadDataSourceClient
