package uk.co.zac_h.spacex.feature.launch

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.paging.compose.collectAsLazyPagingItems
import uk.co.zac_h.spacex.feature.launch.container.LaunchContainerScreen

/**
 * One launch's details: its tabs for details, first stage, crew and news.
 *
 * @param launchId the launch to show, read from the database.
 * @param isFullscreen true when the details fill the screen, so they show a back button.
 * @param navigateUp leaves the details.
 */
@Composable
fun LaunchDetailScreen(
    launchId: String,
    isFullscreen: Boolean,
    navigateUp: () -> Unit,
    viewModel: LaunchDetailViewModel =
        hiltViewModel<LaunchDetailViewModel, LaunchDetailViewModel.Factory>(
            creationCallback = { factory -> factory.create(launchId) }
        )
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val articles = viewModel.articles.collectAsLazyPagingItems()

    when (val state = uiState) {
        LaunchDetailUiState.Loading -> Box(modifier = Modifier.fillMaxSize())

        LaunchDetailUiState.NotFound -> Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "This launch is no longer available")
        }

        is LaunchDetailUiState.Loaded -> LaunchContainerScreen(
            modifier = Modifier.fillMaxSize(),
            launch = state.launch,
            articles = articles,
            isFullscreen = isFullscreen,
            navigateUp = navigateUp
        )
    }
}
