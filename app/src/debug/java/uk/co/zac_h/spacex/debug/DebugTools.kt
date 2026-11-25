package uk.co.zac_h.spacex.debug

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog

/**
 * The debug tools menu, opened from Settings. Only debug builds have it: release builds compile
 * the release version of this file instead, which adds nothing.
 */
object DebugTools {
    const val ROUTE = "debug_tools"
    const val IS_AVAILABLE = true
}

fun NavGraphBuilder.debugToolsDestination(onDismiss: () -> Unit) {
    dialog(DebugTools.ROUTE) {
        DebugToolsDialog(onDismiss = onDismiss)
    }
}
