package uk.co.zac_h.spacex

import androidx.compose.runtime.MutableState

/**
 * Keeps one back stack per top-level destination, so switching tabs keeps each tab's history,
 * as `saveState`/`restoreState` did with Navigation 2.
 *
 * [backStack] is what the user sees: the start destination's stack, followed by the current
 * destination's stack when that isn't the start. Going back from the root of another destination
 * returns to the start destination; going back from the start's root is left to the system.
 *
 * @param R the type that names a top-level destination.
 * @param T the type of a back stack entry.
 * @param startRoute the destination the app opens on.
 * @param stacks the back stack for each top-level destination. Each must hold at least its root.
 * @param currentRoute holds the selected top-level destination.
 */
class TopLevelBackStack<R : Any, T : Any>(
    private val startRoute: R,
    private val stacks: Map<R, MutableList<T>>,
    private val currentRoute: MutableState<R>,
) {

    init {
        require(startRoute in stacks) { "No back stack for the start destination $startRoute" }
        require(stacks.values.all { it.isNotEmpty() }) { "Every back stack needs a root entry" }
    }

    /** The selected top-level destination. */
    val topLevelRoute: R
        get() = currentRoute.value

    /** The entries to display, oldest first. */
    val backStack: List<T>
        get() {
            val start = stacks.getValue(startRoute)
            val current = currentStack
            return if (current === start) start.toList() else start + current
        }

    private val currentStack: MutableList<T>
        get() = stacks[currentRoute.value] ?: stacks.getValue(startRoute)

    /** Switches to [route], keeping the history of the destination being left. */
    fun selectTopLevel(route: R) {
        require(route in stacks) { "No back stack for $route" }
        currentRoute.value = route
    }

    /** Adds [entry] on top of the current destination's stack, unless it's already on top. */
    fun add(entry: T) {
        val stack = currentStack
        if (stack.last() != entry) stack.add(entry)
    }

    /**
     * Adds [entry] on top of the current destination's stack, replacing the top entry instead when
     * [replaces] matches it. Used for detail panes, so opening another item while one is open
     * swaps it rather than stacking them.
     */
    fun addOrReplaceTop(entry: T, replaces: (T) -> Boolean) {
        val stack = currentStack
        val top = stack.last()
        when {
            top == entry -> Unit
            stack.size > 1 && replaces(top) -> stack[stack.lastIndex] = entry
            else -> stack.add(entry)
        }
    }

    /** Goes back one entry. */
    fun removeLast() {
        val stack = currentStack
        when {
            stack.size > 1 -> stack.removeAt(stack.lastIndex)
            currentRoute.value != startRoute -> currentRoute.value = startRoute
        }
    }
}
