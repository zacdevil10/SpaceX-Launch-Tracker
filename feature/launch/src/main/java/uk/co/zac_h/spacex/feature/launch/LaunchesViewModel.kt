package uk.co.zac_h.spacex.feature.launch

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.PREVIOUS_LAUNCHES_PAGE_SIZE
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
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

    init {
        getUpcoming()
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

data class LaunchesUIState(
    val openedLaunch: LaunchItem? = null,
    val isDetailOnlyOpen: Boolean = false
)
