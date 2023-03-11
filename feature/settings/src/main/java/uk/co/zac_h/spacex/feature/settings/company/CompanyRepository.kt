package uk.co.zac_h.spacex.feature.settings.company

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.AgencyDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.AgencyResponse
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    @AgencyDataSourceClient companyDataSource: RemoteDataSource<AgencyResponse>,
    cache: Cache<AgencyResponse>
) : Repository<AgencyResponse>(companyDataSource, cache)
