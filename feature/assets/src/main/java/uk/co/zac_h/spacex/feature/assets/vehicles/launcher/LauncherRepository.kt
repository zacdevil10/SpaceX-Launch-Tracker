package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import androidx.paging.PagingData
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

class LauncherRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /** Stored launchers, fetching each page once and the list again only once it is out of date. */
    fun launchers(): Flow<PagingData<LauncherDetailedDto>> = assetStore.launchers()
}
