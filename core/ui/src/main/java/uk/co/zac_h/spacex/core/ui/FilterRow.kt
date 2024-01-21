package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun FilterRow(
    modifier: Modifier = Modifier,
    label: String,
    content: @Composable () -> Unit
) {
    Row(
        modifier = modifier
    ) {
        Text(
            modifier = Modifier
                .height(48.dp)
                .weight(0.25f)
                .wrapContentHeight(align = Alignment.CenterVertically)
                .padding(start = 16.dp),
            text = label
        )
        Spacer(modifier = Modifier.width(16.dp))
        FlowRow(
            modifier = Modifier.weight(0.8f),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            content()
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
internal fun FilterRowPreview() {
    SpaceXTheme {
        FilterRow(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            label = "Label:"
        ) {
            FilterChip(
                selected = true,
                onClick = {},
                label = { Text(text = "Filter") }
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = "Filter") }
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@ComponentPreviews
@Composable
internal fun FilterRowFlowPreview() {
    SpaceXTheme {
        FilterRow(
            modifier = Modifier.background(MaterialTheme.colorScheme.background),
            label = "Label:"
        ) {
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = "Filter") }
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = "Filter") }
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = "Filter") }
            )
            FilterChip(
                selected = false,
                onClick = {},
                label = { Text(text = "Filter") }
            )
        }
    }
}
