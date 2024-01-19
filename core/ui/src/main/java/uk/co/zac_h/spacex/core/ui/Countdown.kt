package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.TextUnitType
import kotlinx.coroutines.delay

@Composable
fun Countdown(
    modifier: Modifier = Modifier,
    future: Long?,
    onFinishLabel: String
) {
    val diff = future?.minus(System.currentTimeMillis()) ?: 0

    var remaining by remember { mutableLongStateOf(diff) }

    LaunchedEffect(remaining) {
        delay(1000L)
        remaining -= 1000L
    }

    Row(modifier = modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceAround) {
        Text(
            text = if (remaining > 0) String.format(
                "T-%02d:%02d:%02d:%02d",
                remaining.toCountdownDays(),
                remaining.toCountdownHours(),
                remaining.toCountdownMinutes(),
                remaining.toCountdownSeconds()
            ) else onFinishLabel,
            style = MaterialTheme.typography.headlineMedium,
            letterSpacing = TextUnit(3f, TextUnitType.Sp),
            color = MaterialTheme.colorScheme.secondary
        )
    }
}

@DynamicThemePreviews
@ComponentPreviews
@Composable
fun CountdownPreview() {
    SpaceXTheme {
        Countdown(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            0,
            "Countdown"
        )
    }
}