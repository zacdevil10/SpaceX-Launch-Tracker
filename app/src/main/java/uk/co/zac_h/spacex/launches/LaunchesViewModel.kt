package uk.co.zac_h.spacex.launches

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.*
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.dto.spacex.LaunchResponse
import javax.inject.Inject

@HiltViewModel
class LaunchesViewModel @Inject constructor(
    private val repository: LaunchesRepository
) : ViewModel() {

    val upcomingLaunchesLiveData: LiveData<PagingData<LaunchResponse>> = Pager(
        PagingConfig(pageSize = 5)
    ) {
        repository.upcomingLaunchesPagingSource
    }.liveData.cachedIn(viewModelScope)

    val previousLaunchesLiveData: LiveData<PagingData<LaunchResponse>> = Pager(
        PagingConfig(pageSize = 5)
    ) {
        repository.previousLaunchesPagingSource
    }.liveData.cachedIn(viewModelScope)

    var launch: Launch? = null
}
