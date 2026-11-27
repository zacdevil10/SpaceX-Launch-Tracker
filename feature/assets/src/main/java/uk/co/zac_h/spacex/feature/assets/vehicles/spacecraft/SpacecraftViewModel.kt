package uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft

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
class SpacecraftViewModel @Inject constructor(
    private val repository: VehicleRepository
) : ViewModel() {

    val spacecraftLiveData: Flow<PagingData<SpacecraftItem>> =
        repository.spacecraft().map { page -> page.map(::SpacecraftItem) }.cachedIn(viewModelScope)
}
