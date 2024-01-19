package uk.co.zac_h.spacex.feature.settings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val repository: SettingsRepository
) : ViewModel() {

    val settingsUiState: StateFlow<SettingsUiState> = combine(
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

    fun updateDarkThemeConfig(darkThemeConfig: DarkThemeConfig) {
        viewModelScope.launch {
            repository.setDarkThemeConfig(darkThemeConfig)
        }
    }

    fun updateDynamicColorPreferences(useDynamicColor: Boolean) {
        viewModelScope.launch {
            repository.setDynamicColorPreferences(useDynamicColor)
        }
    }
}

data class SettingsUiState(
    val useDynamicColor: Boolean = true,
    val darkThemeConfig: DarkThemeConfig = DarkThemeConfig.FOLLOW_SYSTEM
)

enum class DarkThemeConfig(val value: Int) {
    FOLLOW_SYSTEM(0),
    LIGHT(1),
    DARK(2)
}