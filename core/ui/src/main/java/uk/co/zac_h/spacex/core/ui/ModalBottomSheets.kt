package uk.co.zac_h.spacex.core.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SheetState
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ModalFilterBottomSheet(
    modifier: Modifier = Modifier,
    setSheetState: (Boolean) -> Unit,
    sheetState: SheetState,
    reset: () -> Unit,
    content: @Composable () -> Unit
) {
    ModalBottomSheet(
        modifier = modifier,
        onDismissRequest = {
            setSheetState(false)
        },
        sheetState = sheetState
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 16.dp, bottom = 48.dp)
        ) {
            content()
            OutlinedButton(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .padding(top = 8.dp),
                onClick = { reset() }
            ) {
                Text(text = "Reset")
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@DevicePreviews
@Composable
fun ModalFilterBottomSheetPreview() {
    SpaceXTheme {
        ModalFilterBottomSheet(
            setSheetState = {},
            sheetState = SheetState(
                skipPartiallyExpanded = true,
                initialValue = SheetValue.Expanded
            ),
            reset = {}
        ) {
            FilterRow(label = "Filter:") {
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
}
