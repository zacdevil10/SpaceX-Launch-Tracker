package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val repository: LauncherRepository
) : ViewModel() {

    val launcherLiveData: Flow<PagingData<CoreItem>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.launcherPagingSource
    }.flow.map { data ->
        data.map { CoreItem(it) }
    }.cachedIn(viewModelScope)
}
