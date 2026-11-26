package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

class SpacecraftRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /** Stored spacecraft, fetching each page once and the list again only once it is out of date. */
    fun spacecraft(): Flow<PagingData<SpacecraftNormalDto>> = assetStore.spacecraft()
}
