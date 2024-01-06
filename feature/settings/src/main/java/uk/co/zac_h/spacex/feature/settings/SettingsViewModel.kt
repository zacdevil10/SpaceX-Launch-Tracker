package uk.co.zac_h.spacex.feature.settings

import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import uk.co.zac_h.spacex.feature.settings.theme.ThemePreferenceRepository
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(
    val repository: ThemePreferenceRepository
) : ViewModel() {
}