package uk.co.zac_h.spacex

import androidx.compose.material3.DrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.navigationevent.NavigationEventDispatcher
import androidx.navigationevent.NavigationEventHandler
import androidx.navigationevent.NavigationEventInfo
import androidx.navigationevent.compose.LocalNavigationEventDispatcherOwner
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Closes the modal drawer on system back while it's open.
 *
 * NavDisplay handles back at the default priority, so a plain BackHandler only fires when the
 * navigation host has nothing to pop. This registers at overlay priority, which is consulted
 * first, so back closes the drawer instead of navigating the screen behind it, and NavDisplay
 * doesn't play its predictive pop preview while the drawer is open.
 */
@Composable
fun DrawerBackHandler(drawerState: DrawerState) {
    val scope = rememberCoroutineScope()
    val dispatcher = LocalNavigationEventDispatcherOwner.current?.navigationEventDispatcher ?: return

    val handler = remember(drawerState, scope) { DrawerNavigationEventHandler(drawerState, scope) }
    val isOpen = drawerState.isOpen
    SideEffect { handler.isBackEnabled = isOpen }

    DisposableEffect(dispatcher, handler) {
        dispatcher.addHandler(handler, NavigationEventDispatcher.PRIORITY_OVERLAY)
        onDispose { handler.remove() }
    }
}

private class DrawerNavigationEventHandler(
    private val drawerState: DrawerState,
    private val scope: CoroutineScope
) : NavigationEventHandler<NavigationEventInfo>(
    initialInfo = NavigationEventInfo.None,
    isBackEnabled = false
) {
    override fun onBackCompleted() {
        scope.launch { drawerState.close() }
    }
}
