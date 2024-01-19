package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

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
class SpacecraftViewModel @Inject constructor(
    private val repository: SpacecraftRepository
) : ViewModel() {

    val spacecraftLiveData: Flow<PagingData<SpacecraftItem>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.spacecraftPagingSource
    }.flow.toType(::SpacecraftItem).cachedIn(viewModelScope)
}
