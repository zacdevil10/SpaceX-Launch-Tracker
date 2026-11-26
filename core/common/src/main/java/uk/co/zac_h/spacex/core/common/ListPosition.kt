package uk.co.zac_h.spacex.core.common

import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.snapshotFlow

/** A list's first visible item and how far it is scrolled. */
data class ListPosition(val index: Int = 0, val offset: Int = 0)

/**
 * A [LazyListState] that starts at [initial] and reports each new position to [onPositionChange].
 *
 * Screens shown in a list/detail layout are composed again when they move between the
 * single-pane and two-pane layouts, for example on rotation, and saved state alone doesn't carry
 * over then. Keeping the position in the screen's ViewModel, which both layouts share, does.
 */
@Composable
fun rememberTrackedLazyListState(
    initial: ListPosition,
    onPositionChange: (ListPosition) -> Unit
): LazyListState {
    val state = rememberLazyListState(initial.index, initial.offset)
    val currentOnPositionChange by rememberUpdatedState(onPositionChange)
    LaunchedEffect(state) {
        snapshotFlow { ListPosition(state.firstVisibleItemIndex, state.firstVisibleItemScrollOffset) }
            .collect { currentOnPositionChange(it) }
    }
    return state
}
