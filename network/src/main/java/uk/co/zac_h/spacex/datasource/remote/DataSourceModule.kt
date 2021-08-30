package uk.co.zac_h.spacex.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.datasource.remote.about.CompanyDataSource
import uk.co.zac_h.spacex.datasource.remote.about.HistoryDataSource
import uk.co.zac_h.spacex.dto.spacex.CompanyResponse
import uk.co.zac_h.spacex.dto.spacex.HistoryDocsModel
import uk.co.zac_h.spacex.dto.spacex.LaunchDocsModel
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

}

@Qualifier
annotation class HistoryDataSourceClient

@Qualifier
annotation class CompanyDataSourceClient

@Qualifier
annotation class LaunchesDataSourceClient