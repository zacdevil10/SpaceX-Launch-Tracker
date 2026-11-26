package uk.co.zac_h.spacex.feature.settings.company

import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

class CompanyRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /** The stored SpaceX agency, or null until it has been fetched once. */
    val agency: Flow<AgencyEndpointDetailedDto?> = assetStore.agency

    /** Fetches the agency if it is out of date, or always when [force]. */
    suspend fun refreshAgency(force: Boolean = false): ApiResult<Unit> =
        assetStore.refreshAgency(force)
}
