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
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.apiFlow
import uk.co.zac_h.spacex.network.dto.news.ArticleResponse
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchesUIState())
    val uiState: StateFlow<LaunchesUIState> = _uiState

    val upcomingLaunches: Flow<ApiResult<List<LaunchItem>>> = apiFlow {
        repository.fetch(key = "upcoming_launches", cachePolicy = CachePolicy.EXPIRES)
    }.toType(::LaunchItem)

    val previousLaunchesLiveData: Flow<PagingData<LaunchItem>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.previousLaunchesPagingSource
    }.flow.toType(::LaunchItem).cachedIn(viewModelScope)

    @OptIn(ExperimentalCoroutinesApi::class)
    val articlesFlow: Flow<PagingData<ArticleResponse>> = _uiState.flatMapLatest {
        Pager(
            PagingConfig(pageSize = 10)
        ) {
            repository.articlesPagingSource(it.openedLaunch?.id)
        }.flow
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
