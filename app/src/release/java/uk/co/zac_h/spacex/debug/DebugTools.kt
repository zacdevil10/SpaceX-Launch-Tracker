package uk.co.zac_h.spacex.debug

import androidx.compose.runtime.Composable

/** Release builds have no debug tools. The debug build's version of this file adds them. */
object DebugTools {
    const val IS_AVAILABLE = false

    @Suppress("UNUSED_PARAMETER")
    @Composable
    fun Dialog(onDismiss: () -> Unit) = Unit
}
