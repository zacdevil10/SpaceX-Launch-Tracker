package uk.co.zac_h.spacex.about.company

import uk.co.zac_h.spacex.Cache
import uk.co.zac_h.spacex.Repository
import uk.co.zac_h.spacex.datasource.CompanyDataSourceClient
import uk.co.zac_h.spacex.datasource.RemoteDataSource
import uk.co.zac_h.spacex.dto.spacex.CompanyResponse
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    @CompanyDataSourceClient companyDataSource: RemoteDataSource<CompanyResponse>,
    cache: Cache<CompanyResponse>
) : Repository<CompanyResponse>(companyDataSource, cache)