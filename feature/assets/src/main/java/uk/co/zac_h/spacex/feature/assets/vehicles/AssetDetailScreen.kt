package uk.co.zac_h.spacex.feature.assets.vehicles

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle

/**
 * One asset's details, opened from the Assets lists.
 *
 * @param type which list the asset comes from.
 * @param vehicleId the asset's id within that list.
 * @param isFullscreen true when the details fill the screen, so they show a back button.
 * @param navigateUp leaves the details.
 */
@Composable
fun AssetDetailScreen(
    type: VehicleType,
    vehicleId: String,
    isFullscreen: Boolean,
    navigateUp: () -> Unit,
    viewModel: VehicleDetailViewModel =
        hiltViewModel<VehicleDetailViewModel, VehicleDetailViewModel.Factory>(
            creationCallback = { factory -> factory.create(type, vehicleId) }
        )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()

    when (val state = uiState) {
        VehicleDetailUiState.Loading -> Box(modifier = Modifier.fillMaxSize())

        VehicleDetailUiState.NotFound -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "This item is no longer available")
        }

        is VehicleDetailUiState.Loaded -> VehicleDetailsScreen(
            vehicle = state.vehicle,
            isFullscreen = isFullscreen,
            navigateUp = navigateUp
        )
    }
}
