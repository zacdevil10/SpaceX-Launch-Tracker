package uk.co.zac_h.spacex.feature.assets.astronauts

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.network.toType
import javax.inject.Inject

@HiltViewModel
class AstronautViewModel @Inject constructor(
    private val repository: AstronautRepository
) : ViewModel() {

    val astronautLiveData: Flow<PagingData<AstronautItem>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.astronautPagingSource
    }.flow.toType(::AstronautItem).cachedIn(viewModelScope)
}
