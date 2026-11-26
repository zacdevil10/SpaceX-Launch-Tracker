package uk.co.zac_h.spacex.feature.assets.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn

/** One asset's details, read from the database by type and id. Scoped to its back stack entry. */
@HiltViewModel(assistedFactory = VehicleDetailViewModel.Factory::class)
class VehicleDetailViewModel @AssistedInject constructor(
    @Assisted val type: VehicleType,
    @Assisted val vehicleId: String,
    repository: VehicleDetailRepository
) : ViewModel() {

    val uiState: StateFlow<VehicleDetailUiState> = repository.vehicle(type, vehicleId)
        .map { vehicle ->
            vehicle?.let { VehicleDetailUiState.Loaded(it) } ?: VehicleDetailUiState.NotFound
        }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), VehicleDetailUiState.Loading)

    @AssistedFactory
    interface Factory {
        fun create(type: VehicleType, vehicleId: String): VehicleDetailViewModel
    }
}

sealed interface VehicleDetailUiState {
    data object Loading : VehicleDetailUiState
    data object NotFound : VehicleDetailUiState
    data class Loaded(val vehicle: VehicleItem) : VehicleDetailUiState
}
