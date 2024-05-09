package uk.co.zac_h.spacex.core.ui.component

import androidx.compose.animation.Crossfade
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun <T> SelectableContent(
    value: T?,
    content: @Composable (T) -> Unit,
    label: String
) {
    value?.let {
        Crossfade(targetState = it, label = "") { targetState ->
            content(targetState)
        }
    } ?: Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        contentAlignment = Alignment.Center
    ) {
        Text(text = label)
    }
}
