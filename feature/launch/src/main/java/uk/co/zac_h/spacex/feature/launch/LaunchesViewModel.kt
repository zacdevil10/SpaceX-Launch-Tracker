package uk.co.zac_h.spacex.feature.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.store.Freshness
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    /**
     * The selected list tab and each list's scroll position. The list is composed again when it
     * moves between the single-pane and two-pane layouts, for example on rotation, and saved
     * state alone doesn't carry over then; this ViewModel is shared by both.
     */
    var selectedPage: Int = 0
    var upcomingPosition: ListPosition = ListPosition()
    var previousPosition: ListPosition = ListPosition()

    private val upcomingRefresh = MutableStateFlow<ApiResult<Unit>>(ApiResult.Pending)

    /**
     * Upcoming launches from the database. Stored launches are shown even while a refresh is
     * running or after it failed; an error is shown only when there is nothing stored.
     */
    val upcomingLaunches: Flow<ApiResult<List<LaunchItem>>> =
        combine(repository.upcomingLaunches, upcomingRefresh) { launches, refresh ->
            when {
                launches.isNotEmpty() -> ApiResult.Success(launches.map(::LaunchItem))
                refresh is ApiResult.Failure -> refresh
                refresh is ApiResult.Success -> ApiResult.Success(emptyList())
                else -> ApiResult.Pending
            }
        }

    val previousLaunchesLiveData: Flow<PagingData<LaunchItem>> =
        repository.previousLaunches().toType(::LaunchItem).cachedIn(viewModelScope)

    private val nextLaunchRefresh = MutableStateFlow(NextLaunchRefresh())
    private var manualRefreshAllowedAt = 0L

    /** What the "Updated X ago" row above the next launch shows. */
    val nextLaunchUpdate: Flow<NextLaunchUpdate> =
        combine(repository.nextLaunchFetchedAt, nextLaunchRefresh) { fetchedAt, refresh ->
            NextLaunchUpdate(
                fetchedAt = fetchedAt,
                isRefreshing = refresh.isRefreshing,
                error = refresh.error
            )
        }

    init {
        getUpcoming()
    }

    /**
     * Keeps the next launch current while the launches screen is visible: refreshes it straight
     * away if it is more than a minute old, then every 10 minutes, or every 5 from an hour before
     * launch. Run from the screen with repeatOnLifecycle(STARTED), so it stops when the app is
     * not on screen: nothing refreshes in the background.
     */
    suspend fun keepNextLaunchFresh() {
        while (true) {
            repository.pollNextLaunch()?.let(::onNextLaunchResult)
            delay(repository.nextLaunchPollInterval())
        }
    }

    /** The refresh button. Ignored while refreshing or within 30 seconds of the last tap. */
    fun refreshNextLaunch() {
        val now = System.currentTimeMillis()
        if (nextLaunchRefresh.value.isRefreshing || now < manualRefreshAllowedAt) return
        manualRefreshAllowedAt = now + Freshness.MANUAL_REFRESH_COOLDOWN.inWholeMilliseconds
        nextLaunchRefresh.update { it.copy(isRefreshing = true) }
        viewModelScope.launch {
            onNextLaunchResult(repository.refreshNextLaunch(force = true))
        }
    }

    private fun onNextLaunchResult(result: ApiResult<Unit>) {
        nextLaunchRefresh.value = NextLaunchRefresh(
            isRefreshing = false,
            error = (result as? ApiResult.Failure)?.exception
        )
    }

    /** Refreshes upcoming launches if they are out of date, or always when [force]. */
    fun getUpcoming(force: Boolean = false) {
        upcomingRefresh.value = ApiResult.Pending
        viewModelScope.launch {
            upcomingRefresh.value = repository.refreshUpcoming(force)
        }
    }
}

/** A list's first visible item and how far it is scrolled. */
data class ListPosition(val index: Int = 0, val offset: Int = 0)

private data class NextLaunchRefresh(
    val isRefreshing: Boolean = false,
    val error: Throwable? = null
)

data class NextLaunchUpdate(
    /** When the next launch was last fetched, in milliseconds since the epoch, or null if never. */
    val fetchedAt: Long? = null,
    val isRefreshing: Boolean = false,
    /** Why the last refresh failed, or null if it succeeded. */
    val error: Throwable? = null
)
