package uk.co.zac_h.spacex.feature.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.PREVIOUS_LAUNCHES_PAGE_SIZE
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
import uk.co.zac_h.spacex.network.store.Freshness
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchesUIState())
    val uiState: StateFlow<LaunchesUIState> = _uiState

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

    val previousLaunchesLiveData: Flow<PagingData<LaunchItem>> = Pager(
        PagingConfig(
            pageSize = PREVIOUS_LAUNCHES_PAGE_SIZE,
            initialLoadSize = PREVIOUS_LAUNCHES_PAGE_SIZE
        )
    ) {
        repository.previousLaunchesPagingSource
    }.flow.toType(::LaunchItem).cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val articlesFlow: Flow<PagingData<ArticleResponse>> = _uiState.flatMapLatest {
        it.openedLaunch?.let {
            Pager(
                PagingConfig(pageSize = 10)
            ) {
                repository.articlesPagingSource(it.id)
            }.flow
        } ?: flowOf(PagingData.empty())
    }

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

    fun setOpenedLaunch(launch: LaunchItem, contentType: ContentType) {
        _uiState.value = LaunchesUIState(
            openedLaunch = launch,
            isDetailOnlyOpen = contentType == ContentType.SINGLE_PANE
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState.value.copy(isDetailOnlyOpen = false)
    }
}

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

data class LaunchesUIState(
    val openedLaunch: LaunchItem? = null,
    val isDetailOnlyOpen: Boolean = false
)
