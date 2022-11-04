package uk.co.zac_h.spacex.feature.company

import uk.co.zac_h.spacex.network.Cache
import uk.co.zac_h.spacex.network.Repository
import uk.co.zac_h.spacex.network.datasource.remote.CompanyDataSourceClient
import uk.co.zac_h.spacex.network.datasource.remote.RemoteDataSource
import uk.co.zac_h.spacex.network.dto.spacex.CompanyResponse
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    @CompanyDataSourceClient companyDataSource: RemoteDataSource<CompanyResponse>,
    cache: Cache<CompanyResponse>
) : Repository<CompanyResponse>(companyDataSource, cache)
