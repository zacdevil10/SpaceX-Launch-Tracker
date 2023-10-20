package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.dto.spacex.LauncherResponse
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val repository: LauncherRepository
) : ViewModel() {

    val launcherLiveData: LiveData<PagingData<LauncherResponse>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.launcherPagingSource
    }.liveData.cachedIn(viewModelScope)
}
