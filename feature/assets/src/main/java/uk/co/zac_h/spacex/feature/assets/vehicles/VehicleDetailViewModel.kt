package uk.co.zac_h.spacex.feature.assets.vehicles

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import uk.co.zac_h.spacex.core.data.AstronautRepository
import uk.co.zac_h.spacex.core.data.VehicleRepository
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautItem
import uk.co.zac_h.spacex.feature.assets.vehicles.dragon.SecondStageItem
import uk.co.zac_h.spacex.feature.assets.vehicles.launcher.CoreItem
import uk.co.zac_h.spacex.feature.assets.vehicles.rockets.RocketItem
import uk.co.zac_h.spacex.feature.assets.vehicles.spacecraft.SpacecraftItem

/** One asset's details, read from the database by type and id. Scoped to its back stack entry. */
@HiltViewModel(assistedFactory = VehicleDetailViewModel.Factory::class)
class VehicleDetailViewModel @AssistedInject constructor(
    @Assisted val type: VehicleType,
    @Assisted val vehicleId: String,
    vehicles: VehicleRepository,
    astronauts: AstronautRepository
) : ViewModel() {

    /**
     * The stored asset, or null while it isn't stored. Every asset the lists show is stored, so
     * one opened from them is always found.
     */
    private val vehicle: Flow<VehicleItem?> = when (type) {
        VehicleType.ROCKET -> vehicles.rocket(vehicleId).map { it?.let(::RocketItem) }
        VehicleType.SECOND_STAGE ->
            vehicles.spacecraftConfiguration(vehicleId).map { it?.let(::SecondStageItem) }
        VehicleType.CORE -> vehicles.launcher(vehicleId).map { it?.let(::CoreItem) }
        VehicleType.CAPSULE -> vehicles.spacecraft(vehicleId).map { it?.let(::SpacecraftItem) }
        VehicleType.ASTRONAUT -> astronauts.astronaut(vehicleId).map { it?.let(::AstronautItem) }
    }

    val uiState: StateFlow<VehicleDetailUiState> = vehicle
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
