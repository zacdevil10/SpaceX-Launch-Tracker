package uk.co.zac_h.spacex.core.data

import androidx.paging.PagingData
import androidx.paging.map
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.data.mapper.toAstronaut
import uk.co.zac_h.spacex.core.model.Astronaut
import uk.co.zac_h.spacex.network.store.AssetStore
import javax.inject.Inject

/** SpaceX astronauts, from the Room cache that [AssetStore] keeps current. */
class AstronautRepository @Inject constructor(
    private val assetStore: AssetStore
) {

    /** Stored astronauts, fetching each page once and the list again only once it is out of date. */
    fun astronauts(): Flow<PagingData<Astronaut>> =
        assetStore.astronauts().map { page -> page.map { it.toAstronaut() } }

    /** One stored astronaut, or null while it isn't stored. */
    fun astronaut(id: String): Flow<Astronaut?> = assetStore.astronaut(id).map { it?.toAstronaut() }
}
