package uk.co.zac_h.spacex.network.store

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import uk.co.zac_h.spacex.core.database.LaunchEntity
import uk.co.zac_h.spacex.network.ApiResult
import kotlin.coroutines.cancellation.CancellationException

/**
 * Fills the launches table as the previous launches list is opened and scrolled. The list itself
 * always reads from the database.
 *
 * - Opening: skipped if changes were checked within the hour, else one small call for changes
 *   (see [LaunchStore.refreshPreviousLaunches]).
 * - Scrolling past the oldest stored launch: one call for the next older page.
 */
@OptIn(ExperimentalPagingApi::class)
class PreviousLaunchesMediator(
    private val store: LaunchStore
) : RemoteMediator<Int, LaunchEntity>() {

    override suspend fun initialize(): InitializeAction =
        if (store.previousLaunchesAreFresh()) {
            InitializeAction.SKIP_INITIAL_REFRESH
        } else {
            InitializeAction.LAUNCH_INITIAL_REFRESH
        }

    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, LaunchEntity>
    ): MediatorResult = when (loadType) {
        // The list starts at the most recent launch, so there is never anything before it.
        LoadType.PREPEND -> MediatorResult.Success(endOfPaginationReached = true)
        LoadType.REFRESH -> when (val result = store.refreshPreviousLaunches(pageSize = state.config.pageSize)) {
            is ApiResult.Failure -> MediatorResult.Error(result.exception)
            else -> MediatorResult.Success(endOfPaginationReached = false)
        }
        LoadType.APPEND -> try {
            MediatorResult.Success(
                endOfPaginationReached = store.loadOlderPreviousLaunches(state.config.pageSize)
            )
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            MediatorResult.Error(e.toApiException())
        }
    }
}
