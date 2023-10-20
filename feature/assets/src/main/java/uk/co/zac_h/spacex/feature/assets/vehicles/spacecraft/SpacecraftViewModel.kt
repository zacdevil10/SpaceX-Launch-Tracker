package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.paging.liveData
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.network.dto.spacex.SpacecraftResponse
import javax.inject.Inject

@HiltViewModel
class SpacecraftViewModel @Inject constructor(
    private val repository: SpacecraftRepository
) : ViewModel() {

    val spacecraftLiveData: LiveData<PagingData<SpacecraftResponse>> = Pager(
        PagingConfig(pageSize = 10)
    ) {
        repository.spacecraftPagingSource
    }.liveData.cachedIn(viewModelScope)

    var selectedSpacecraft: SpacecraftItem? = null
}