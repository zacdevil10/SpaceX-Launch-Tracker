package uk.co.zac_h.spacex.network.store

import kotlinx.serialization.KSerializer
import uk.co.zac_h.spacex.core.database.CachedItemDao
import uk.co.zac_h.spacex.core.database.FetchMetadataDao
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.retrofit.networkJson
import javax.inject.Inject
import javax.inject.Singleton

/**
 * The database-backed lists that are the app's source of truth for Launch Library data. One
 * instance per list, so each list's refreshes are serialised (see [CachedList.refresh]).
 */
@Singleton
class LaunchLibraryStore @Inject constructor(
    private val itemDao: CachedItemDao,
    private val metadataDao: FetchMetadataDao,
    private val clock: Clock
) {

    val upcomingLaunches: CachedList<LaunchDetailedDto> =
        list(UPCOMING_LAUNCHES, LaunchDetailedDto.serializer()) { it.id }

    private fun <T> list(key: String, serializer: KSerializer<T>, id: (T) -> String) =
        CachedList(key, serializer, id, itemDao, metadataDao, networkJson, clock)

    companion object {
        const val UPCOMING_LAUNCHES = "launches_upcoming"
    }
}
