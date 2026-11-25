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
import uk.co.zac_h.spacex.network.LAUNCH_LIBRARY_PAGE_SIZE
import javax.inject.Inject

@HiltViewModel
class AstronautViewModel @Inject constructor(
    private val repository: AstronautRepository
) : ViewModel() {

    val astronautLiveData: Flow<PagingData<AstronautItem>> = Pager(
        PagingConfig(
            pageSize = LAUNCH_LIBRARY_PAGE_SIZE,
            initialLoadSize = LAUNCH_LIBRARY_PAGE_SIZE
        )
    ) {
        repository.astronautPagingSource
    }.flow.toType(::AstronautItem).cachedIn(viewModelScope)
}
