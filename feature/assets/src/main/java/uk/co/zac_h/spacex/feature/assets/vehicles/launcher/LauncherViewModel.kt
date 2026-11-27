package uk.co.zac_h.spacex.feature.assets.vehicles.launcher

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.map
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import uk.co.zac_h.spacex.core.data.VehicleRepository
import javax.inject.Inject

@HiltViewModel
class LauncherViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    val launcherLiveData: Flow<PagingData<CoreItem>> =
        repository.launchers().map { page -> page.map(::CoreItem) }.cachedIn(viewModelScope)
}
