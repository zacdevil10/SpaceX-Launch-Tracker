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
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
import uk.co.zac_h.spacex.network.dto.spacex.mapResults
import uk.co.zac_h.spacex.network.get
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchesUIState())
    val uiState: StateFlow<LaunchesUIState> = _uiState

    private val _upcomingLaunches: MutableStateFlow<ApiResult<List<LaunchItem>>> =
        MutableStateFlow(ApiResult.Pending)
    val upcomingLaunches: Flow<ApiResult<List<LaunchItem>>> = _upcomingLaunches

    val previousLaunchesLiveData: Flow<PagingData<LaunchItem>> = Pager(
        PagingConfig(pageSize = 5)
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

    fun getUpcoming() {
        _upcomingLaunches.get(viewModelScope) {
            repository.fetch(key = "", cachePolicy = CachePolicy.REFRESH)
                .mapResults(::LaunchItem)
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
