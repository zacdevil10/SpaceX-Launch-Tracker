package uk.co.zac_h.spacex.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.datasource.remote.about.CompanyDataSource
import uk.co.zac_h.spacex.datasource.remote.about.HistoryDataSource
import uk.co.zac_h.spacex.datasource.remote.statistics.LandingPadDataSource
import uk.co.zac_h.spacex.datasource.remote.statistics.LaunchpadDataSource
import uk.co.zac_h.spacex.datasource.remote.vehicles.DragonDataSource
import uk.co.zac_h.spacex.datasource.remote.vehicles.RocketDataSource
import uk.co.zac_h.spacex.datasource.remote.vehicles.ShipDataSource
import uk.co.zac_h.spacex.dto.spacex.*
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    @HistoryDataSourceClient
    abstract fun bindHistoryDataSource(historyDataSource: HistoryDataSource): RemoteDataSource<HistoryDocsModel>

    @Binds
    @CompanyDataSourceClient
    abstract fun bindCompanyDataSource(companyDataSource: CompanyDataSource): RemoteDataSource<CompanyResponse>

    @Binds
    @LaunchesDataSourceClient
    abstract fun bindLaunchesDataSource(launchesDataSource: LaunchesDataSource): RemoteDataSource<LaunchDocsModel>

    @Binds
    @LaunchpadDataSourceClient
    abstract fun bindLaunchpadDataSource(launchpadDataSource: LaunchpadDataSource): RemoteDataSource<LaunchpadDocsModel>

    @Binds
    @LandingPadDataSourceClient
    abstract fun bindLandingPadDataSource(landingPadDataSource: LandingPadDataSource): RemoteDataSource<LandingPadDocsModel>

    @Binds
    @RocketDataSourceClient
    abstract fun bindRocketDataSource(rocketDataSource: RocketDataSource): RemoteDataSource<MutableList<RocketResponse>>

    @Binds
    @DragonDataSourceClient
    abstract fun bindDragonDataSource(dragonDataSource: DragonDataSource): RemoteDataSource<MutableList<DragonResponse>>

    @Binds
    @ShipDataSourceClient
    abstract fun bindShipDataSource(shipDataSource: ShipDataSource): RemoteDataSource<ShipsDocsModel>

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