package uk.co.zac_h.spacex.feature.launch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.async
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(LaunchesUIState())
    val uiState: StateFlow<LaunchesUIState> = _uiState

    private val _upcomingLaunchesLiveData = MutableLiveData<ApiResult<List<LaunchItem>>>()
    val upcomingLaunchesLiveData: LiveData<ApiResult<List<LaunchItem>>> = _upcomingLaunchesLiveData

    val previousLaunchesLiveData: Flow<PagingData<LaunchItem>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.previousLaunchesPagingSource
    }.flow.map { data ->
        data.map { LaunchItem(it) }
    }.cachedIn(viewModelScope)

    fun getUpcomingLaunches(cachePolicy: CachePolicy = CachePolicy.ALWAYS) {
        viewModelScope.launch {
            val response = async(_upcomingLaunchesLiveData) {
                repository.fetch(key = "upcoming_launches", cachePolicy = cachePolicy)
            }

            _upcomingLaunchesLiveData.value = response.await().map {
                it.results.map { launch -> LaunchItem(launch) }
            }
        }
    }

    fun setOpenedLaunch(launch: LaunchItem, contentType: ContentType) {
        _uiState.value = LaunchesUIState(
            openedLaunch = launch,
            isDetailOnlyOpen = contentType == ContentType.SINGLE_PANE
        )
    }

    fun closeDetailScreen() {
        _uiState.value = LaunchesUIState()
    }
}

data class LaunchesUIState(
    val openedLaunch: LaunchItem? = null,
    val isDetailOnlyOpen: Boolean = false
)
