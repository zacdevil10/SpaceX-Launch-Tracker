package uk.co.zac_h.spacex.core.ui.component

import androidx.compose.material3.ColorScheme
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.surfaceColorAtElevation
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.lerp
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBarScrollBehavior?.containerColor() = lerp(
    MaterialTheme.colorScheme.surface,
    MaterialTheme.colorScheme.applyTonalElevation(
        backgroundColor = MaterialTheme.colorScheme.surface,
        elevation = 3.dp
    ),
    this?.state?.overlappedFraction.containerColorFraction()
)

private fun Float?.containerColorFraction(): Float {
    val colorTransitionFraction = this ?: 0f

    return if (colorTransitionFraction > 0.01f) 1f else 0f
}

private fun ColorScheme.applyTonalElevation(
    backgroundColor: Color,
    elevation: Dp
): Color = if (backgroundColor == surface) {
    surfaceColorAtElevation(elevation)
} else {
    backgroundColor
}