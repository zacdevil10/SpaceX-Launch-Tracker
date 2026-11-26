package uk.co.zac_h.spacex.network.store

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import uk.co.zac_h.spacex.core.database.ListItemEntity
import uk.co.zac_h.spacex.network.ApiResult
import kotlin.coroutines.cancellation.CancellationException

/**
 * Fills a [ListStore] as its list is opened and scrolled. Opening is skipped while the list is
 * fresh; scrolling past the last stored item fetches the next page.
 */
@OptIn(ExperimentalPagingApi::class)
internal class ListItemsMediator(
    private val store: ListStore<*>
) : RemoteMediator<Int, ListItemEntity>() {

    override suspend fun initialize(): InitializeAction =
        if (store.isFresh()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, ListItemEntity>
    ): MediatorResult = when (loadType) {
        LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
        LoadType.REFRESH -> when (val result = store.refresh(state.config.pageSize)) {
            is ApiResult.Failure -> MediatorResult.Error(result.exception)
            else -> MediatorResult.Success(endOfPaginationReached = false)
        }
        LoadType.APPEND -> try {
            MediatorResult.Success(endOfPaginationReached = store.loadMore(state.config.pageSize))
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e.toApiException())
        }
    }
}
