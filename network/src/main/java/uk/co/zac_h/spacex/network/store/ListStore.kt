package uk.co.zac_h.spacex.network.store

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.flatMap
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.serialization.KSerializer
import uk.co.zac_h.spacex.core.database.ListItemDao
import uk.co.zac_h.spacex.core.database.ListItemEntity
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.retrofit.networkJson
import kotlin.time.Duration

/**
 * A paged Launch Library list (launchers, spacecraft, astronauts) with the database as the source
 * of truth. The list reads from the database; [ListItemsMediator] fetches each page once and
 * fetches the first page again only when the list is older than [maxAge].
 *
 * @param list the name the items are stored under, also the key of its fetch time.
 */
internal class ListStore<T : Any>(
    private val list: String,
    private val maxAge: Duration,
    private val serializer: KSerializer<T>,
    private val id: (T) -> Any,
    private val listItemDao: ListItemDao,
    private val refresher: Refresher,
    private val fetch: suspend (limit: Int, offset: Int) -> LaunchLibraryPaginatedResponse<T>
) {

    @OptIn(ExperimentalPagingApi::class)
    fun items(pageSize: Int): Flow<PagingData<T>> = Pager(
        config = PagingConfig(pageSize = pageSize, initialLoadSize = pageSize),
        remoteMediator = ListItemsMediator(this)
    ) {
        listItemDao.pagingSource(list)
    }.flow.map { page ->
        page.flatMap { listOfNotNull(decode(it)) }
    }

    suspend fun isFresh(): Boolean = !refresher.isStale(list, maxAge)

    /**
     * Replaces the stored list with its first page, if older than [maxAge] or if [force]. Older
     * pages are fetched again as the list scrolls.
     */
    suspend fun refresh(pageSize: Int, force: Boolean = false): ApiResult<Unit> =
        refresher.refresh(list, maxAge, force) {
            val page = fetch(pageSize, 0).results
            listItemDao.replaceList(list, page.toEntities(startPosition = 0))
        }

    /** Fetches the page after the stored items. Returns true when there are no more. */
    suspend fun loadMore(pageSize: Int): Boolean {
        val offset = listItemDao.count(list)
        val response = fetch(pageSize, offset)
        listItemDao.upsertAll(response.results.toEntities(startPosition = offset))
        return response.next == null || response.results.isEmpty()
    }

    private fun List<T>.toEntities(startPosition: Int) = mapIndexed { index, item ->
        ListItemEntity(
            list = list,
            id = id(item).toString(),
            position = startPosition + index,
            json = networkJson.encodeToString(serializer, item)
        )
    }

    private fun decode(entity: ListItemEntity): T? =
        runCatching { networkJson.decodeFromString(serializer, entity.json) }.getOrNull()
}
