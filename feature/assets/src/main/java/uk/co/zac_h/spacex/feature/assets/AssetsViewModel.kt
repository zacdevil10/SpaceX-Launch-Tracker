package uk.co.zac_h.spacex.feature.assets

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import uk.co.zac_h.spacex.core.common.ContentType
import uk.co.zac_h.spacex.feature.assets.astronauts.AstronautItem
import uk.co.zac_h.spacex.feature.assets.vehicles.VehicleItem
import javax.inject.Inject

@HiltViewModel
class AssetsViewModel @Inject constructor() : ViewModel() {

    private val _uiState = MutableStateFlow(AssetsUIState())
    val uiState: StateFlow<AssetsUIState> = _uiState

    fun setOpenedAsset(vehicleItem: VehicleItem, openedAssetPage: Int?, contentType: ContentType) {
        _uiState.value = AssetsUIState(
            openedAsset = vehicleItem,
            openedAssetPage = openedAssetPage,
            isDetailOnlyOpen = contentType == ContentType.SINGLE_PANE && vehicleItem !is AstronautItem
        )
    }

    fun closeDetailScreen() {
        _uiState.value = _uiState.value.copy(isDetailOnlyOpen = false)
    }
}

data class AssetsUIState(
    val openedAsset: VehicleItem? = null,
    val openedAssetPage: Int? = null,
    val isDetailOnlyOpen: Boolean = false
)
