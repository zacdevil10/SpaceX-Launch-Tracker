package uk.co.zac_h.spacex.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import uk.co.zac_h.spacex.feature.settings.SettingsRepository
import uk.co.zac_h.spacex.feature.settings.SettingsUiState
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    repository: SettingsRepository
) : ViewModel() {

    val settings: Flow<SettingsUiState> = combine(
        repository.darkThemeConfigFlow,
        repository.useDynamicColorFlow
    ) { darkThemeConfig, useDynamicColor ->
        SettingsUiState(
            useDynamicColor = useDynamicColor,
            darkThemeConfig = darkThemeConfig
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        initialValue = SettingsUiState()
    )
}
