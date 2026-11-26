package uk.co.zac_h.spacex.debug

import androidx.compose.runtime.Composable

/**
 * The debug tools menu, opened from Settings. Only debug builds have it: release builds compile
 * the release version of this file instead, which shows nothing.
 */
object DebugTools {
    const val IS_AVAILABLE = true

    @Composable
    fun Dialog(onDismiss: () -> Unit) = DebugToolsDialog(onDismiss = onDismiss)
}
