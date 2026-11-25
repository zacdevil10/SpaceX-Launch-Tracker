package uk.co.zac_h.spacex.debug

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.network.LaunchLibraryEnvironment
import uk.co.zac_h.spacex.network.debug.DebugSettings
import javax.inject.Inject

@HiltViewModel
class DebugToolsViewModel @Inject constructor(
    private val settings: DebugSettings
) : ViewModel() {

    val uiState: StateFlow<DebugToolsUiState?> = settings.launchLibraryEnvironment
        .map { DebugToolsUiState(launchLibraryEnvironment = it) }
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), null)

    /** Saves the new server and clears the database, then calls [onSwitched] to restart the app. */
    fun setLaunchLibraryEnvironment(environment: LaunchLibraryEnvironment, onSwitched: () -> Unit) {
        if (environment == uiState.value?.launchLibraryEnvironment) return
        viewModelScope.launch {
            settings.setLaunchLibraryEnvironment(environment)
            onSwitched()
        }
    }
}

data class DebugToolsUiState(
    val launchLibraryEnvironment: LaunchLibraryEnvironment
)
