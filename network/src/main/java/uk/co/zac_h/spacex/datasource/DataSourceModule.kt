package uk.co.zac_h.spacex.datasource

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.dto.spacex.CompanyResponse
import uk.co.zac_h.spacex.dto.spacex.HistoryDocsModel
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

}

@Qualifier
annotation class HistoryDataSourceClient

@Qualifier
annotation class CompanyDataSourceClient