package uk.co.zac_h.spacex.feature.astronauts

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.dto.spacex.AstronautResponse
import javax.inject.Inject

@HiltViewModel
class AstronautViewModel @Inject constructor(
    private val repository: AstronautRepository
) : ViewModel() {

    val astronautLiveData: LiveData<PagingData<AstronautResponse>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.astronautPagingSource
    }.liveData.cachedIn(viewModelScope)
}
