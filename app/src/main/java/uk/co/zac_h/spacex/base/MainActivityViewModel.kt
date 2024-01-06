package uk.co.zac_h.spacex.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.asFlow
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import uk.co.zac_h.spacex.feature.settings.theme.ThemePreferenceRepository
import javax.inject.Inject

@HiltViewModel
class MainActivityViewModel @Inject constructor(
    repository: ThemePreferenceRepository
) : ViewModel() {

    val theme: Flow<Int> = repository.themeModeLive.asFlow()
}