package uk.co.zac_h.spacex.feature.assets.astronauts

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

class AstronautRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /** Stored astronauts, fetching each page once and the list again only once it is out of date. */
    fun astronauts(): Flow<PagingData<AstronautEndpointNormalDto>> = assetStore.astronauts()
}
