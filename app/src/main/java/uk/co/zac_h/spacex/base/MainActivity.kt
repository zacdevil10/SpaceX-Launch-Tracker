package uk.co.zac_h.spacex.base

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.SpaceXApp
import uk.co.zac_h.spacex.core.ui.SpaceXTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            SpaceXTheme {
                val windowSize = calculateWindowSizeClass(this)

                SpaceXApp(
                    windowSize = windowSize
                )
            }
        }
    }
}
