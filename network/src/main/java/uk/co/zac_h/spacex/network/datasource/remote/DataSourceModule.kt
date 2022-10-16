package uk.co.zac_h.spacex.network.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.network.datasource.remote.about.CompanyDataSource
import uk.co.zac_h.spacex.network.datasource.remote.about.HistoryDataSource
import uk.co.zac_h.spacex.network.datasource.remote.statistics.LandingPadDataSource
import uk.co.zac_h.spacex.network.datasource.remote.statistics.LaunchpadDataSource
import uk.co.zac_h.spacex.network.datasource.remote.vehicles.*
import uk.co.zac_h.spacex.network.dto.spacex.*
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    @HistoryDataSourceClient
    abstract fun bindHistoryDataSource(historyDataSource: HistoryDataSource): RemoteDataSource<NetworkDocsResponse<HistoryResponse>>

    @Binds
    @CompanyDataSourceClient
    abstract fun bindCompanyDataSource(companyDataSource: CompanyDataSource): RemoteDataSource<CompanyResponse>

    @Binds
    @LaunchesDataSourceClient
    abstract fun bindLaunchesDataSource(launchesDataSource: LaunchesDataSource): RemoteDataSource<LaunchLibraryPaginatedResponse<LaunchResponse>>

    @Binds
    @LaunchpadDataSourceClient
    abstract fun bindLaunchpadDataSource(launchpadDataSource: LaunchpadDataSource): RemoteDataSource<NetworkDocsResponse<LaunchpadQueriedResponse>>

    @Binds
    @LandingPadDataSourceClient
    abstract fun bindLandingPadDataSource(landingPadDataSource: LandingPadDataSource): RemoteDataSource<NetworkDocsResponse<LandingPadQueriedResponse>>

    @Binds
    @RocketDataSourceClient
    abstract fun bindRocketDataSource(rocketDataSource: RocketDataSource): RemoteDataSource<MutableList<RocketResponse>>

    @Binds
    @DragonDataSourceClient
    abstract fun bindDragonDataSource(dragonDataSource: DragonDataSource): RemoteDataSource<MutableList<DragonResponse>>

    @Binds
    @ShipDataSourceClient
    abstract fun bindShipDataSource(shipDataSource: ShipDataSource): RemoteDataSource<NetworkDocsResponse<ShipQueriedResponse>>

    @Binds
    @CoreDataSourceClient
    abstract fun bindCoreDataSource(coreDataSource: CoreDataSource): RemoteDataSource<NetworkDocsResponse<CoreQueriedResponse>>

    @Binds
    @CapsuleDataSourceClient
    abstract fun bindCapsuleDataSource(capsuleDataSource: CapsuleDataSource): RemoteDataSource<NetworkDocsResponse<CapsuleQueriedResponse>>

    @Binds
    @CrewDataSourceClient
    abstract fun bindCrewDataSource(crewDataSource: CrewDataSource): RemoteDataSource<NetworkDocsResponse<CrewQueriedResponse>>
}

@Qualifier
annotation class HistoryDataSourceClient

@Qualifier
annotation class CompanyDataSourceClient

@Qualifier
annotation class LaunchesDataSourceClient

@Qualifier
annotation class LaunchpadDataSourceClient

@Qualifier
annotation class LandingPadDataSourceClient

@Qualifier
annotation class RocketDataSourceClient

@Qualifier
annotation class DragonDataSourceClient

@Qualifier
annotation class ShipDataSourceClient

@Qualifier
annotation class CoreDataSourceClient

@Qualifier
annotation class CapsuleDataSourceClient

@Qualifier
annotation class CrewDataSourceClient
