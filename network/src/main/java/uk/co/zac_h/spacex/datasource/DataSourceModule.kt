package uk.co.zac_h.spacex.datasource

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Response
import uk.co.zac_h.spacex.dto.spacex.CompanyResponse
import uk.co.zac_h.spacex.dto.spacex.HistoryDocsModel
import javax.inject.Qualifier

@InstallIn(SingletonComponent::class)
@Module
abstract class DataSourceModule {

    @HistoryDataSourceClient
    @Binds
    abstract fun bindHistoryDataSource(historyDataSource: HistoryDataSource): RemoteDataSource<HistoryDocsModel>

    @CompanyDataSourceClient
    @Binds
    abstract fun bindCompanyDataSource(companyDataSource: CompanyDataSource): RemoteDataSource<CompanyResponse>

}

@Qualifier
annotation class HistoryDataSourceClient

@Qualifier
annotation class CompanyDataSourceClient