package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatDelegate
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import uk.co.zac_h.spacex.SpaceXApp
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    private val viewModel: MainActivityViewModel by viewModels()

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        var theme: Int by mutableIntStateOf(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)

        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.theme
                    .onEach { theme = it }
                    .collect()
            }
        }

        setContent {
            val darkTheme = shouldUseDarkTheme(theme)

            SpaceXTheme(darkTheme = darkTheme) {
                val windowSize = calculateWindowSizeClass(this)

                SpaceXApp(
                    widthSize = windowSize.widthSizeClass
                )
            }
        }
    }
}

@Composable
fun shouldUseDarkTheme(theme: Int): Boolean = when (theme) {
    AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM -> isSystemInDarkTheme()
    AppCompatDelegate.MODE_NIGHT_NO -> false
    AppCompatDelegate.MODE_NIGHT_YES -> true
    else -> isSystemInDarkTheme()
}
