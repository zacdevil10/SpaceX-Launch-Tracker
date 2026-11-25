package uk.co.zac_h.spacex.debug

import androidx.navigation.NavGraphBuilder

/** Release builds have no debug tools. The debug build's version of this file adds them. */
object DebugTools {
    const val ROUTE = "debug_tools"
    const val IS_AVAILABLE = false
}

@Suppress("UnusedReceiverParameter", "UNUSED_PARAMETER")
fun NavGraphBuilder.debugToolsDestination(onDismiss: () -> Unit) = Unit
