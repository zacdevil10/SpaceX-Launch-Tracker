package uk.co.zac_h.spacex.launches

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.launches.adapters.Header
import uk.co.zac_h.spacex.launches.adapters.RecyclerViewItem
import uk.co.zac_h.spacex.network.ApiResult
import uk.co.zac_h.spacex.network.CachePolicy
import uk.co.zac_h.spacex.network.async
import uk.co.zac_h.spacex.network.dto.spacex.LaunchResponse
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    private val _upcomingLaunchesLiveData = MutableLiveData<ApiResult<List<LaunchItem>>>()
    val upcomingLaunchesLiveData: LiveData<ApiResult<List<LaunchItem>>> = _upcomingLaunchesLiveData

    val previousLaunchesLiveData: LiveData<PagingData<LaunchResponse>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.previousLaunchesPagingSource
    }.liveData.cachedIn(viewModelScope)

    var launch: LaunchItem? = null

    val cores: List<RecyclerViewItem>
        get() {
            return launch?.firstStage?.apply {
                if (this.size > 1) groupBy { it.type }.flatMap { listOf(Header(it.key.type)) + it.value }
            } ?: emptyList()
        }

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
}
