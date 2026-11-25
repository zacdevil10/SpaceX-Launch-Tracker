package uk.co.zac_h.spacex.network.datasource.remote

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import uk.co.zac_h.spacex.network.datasource.remote.about.AgencyDataSource
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import javax.inject.Qualifier

@Module
@InstallIn(ViewModelComponent::class)
abstract class DataSourceModule {

    @Binds
    @AgencyDataSourceClient
    abstract fun bindAgencyDataSource(agencyDataSource: AgencyDataSource): RemoteDataSource<AgencyEndpointDetailedDto>
}

@Qualifier
annotation class AgencyDataSourceClient
