package uk.co.zac_h.spacex

import androidx.compose.runtime.mutableStateOf
import org.junit.Assert.assertEquals
import org.junit.Test

class TopLevelBackStackTest {

    private enum class Tab { HOME, NEWS, MORE }

    private val stacks = mapOf(
        Tab.HOME to mutableListOf("home"),
        Tab.NEWS to mutableListOf("news"),
        Tab.MORE to mutableListOf("more"),
    )
    private val current = mutableStateOf(Tab.HOME)
    private val backStack = TopLevelBackStack(Tab.HOME, stacks, current)

    @Test
    fun `starts on the start destination`() {
        assertEquals(Tab.HOME, backStack.topLevelRoute)
        assertEquals(listOf("home"), backStack.backStack)
    }

    @Test
    fun `selecting another destination shows it above the start destination`() {
        backStack.selectTopLevel(Tab.NEWS)

        assertEquals(Tab.NEWS, backStack.topLevelRoute)
        assertEquals(listOf("home", "news"), backStack.backStack)
    }

    @Test
    fun `back from another destination's root returns to the start destination`() {
        backStack.selectTopLevel(Tab.NEWS)

        backStack.removeLast()

        assertEquals(Tab.HOME, backStack.topLevelRoute)
        assertEquals(listOf("home"), backStack.backStack)
    }

    @Test
    fun `back from the start destination's root does nothing`() {
        backStack.removeLast()

        assertEquals(Tab.HOME, backStack.topLevelRoute)
        assertEquals(listOf("home"), backStack.backStack)
    }

    @Test
    fun `back pops entries added to the current destination first`() {
        backStack.selectTopLevel(Tab.NEWS)
        backStack.add("article")

        backStack.removeLast()

        assertEquals(Tab.NEWS, backStack.topLevelRoute)
        assertEquals(listOf("home", "news"), backStack.backStack)
    }

    @Test
    fun `switching destinations keeps each destination's history`() {
        backStack.selectTopLevel(Tab.NEWS)
        backStack.add("article")
        backStack.selectTopLevel(Tab.MORE)

        assertEquals(listOf("home", "more"), backStack.backStack)

        backStack.selectTopLevel(Tab.NEWS)

        assertEquals(listOf("home", "news", "article"), backStack.backStack)
    }

    @Test
    fun `reselecting the current destination keeps its history`() {
        backStack.selectTopLevel(Tab.NEWS)
        backStack.add("article")

        backStack.selectTopLevel(Tab.NEWS)

        assertEquals(listOf("home", "news", "article"), backStack.backStack)
    }

    @Test
    fun `adding the entry already on top does not add it twice`() {
        backStack.add("dialog")
        backStack.add("dialog")

        assertEquals(listOf("home", "dialog"), backStack.backStack)
    }

    @Test
    fun `the selected destination is read from the state it was given`() {
        current.value = Tab.MORE

        val restored = TopLevelBackStack(Tab.HOME, stacks, current)

        assertEquals(Tab.MORE, restored.topLevelRoute)
        assertEquals(listOf("home", "more"), restored.backStack)
    }

    @Test(expected = IllegalArgumentException::class)
    fun `selecting a destination without a back stack fails`() {
        TopLevelBackStack(Tab.HOME, mapOf(Tab.HOME to mutableListOf("home")), current)
            .selectTopLevel(Tab.NEWS)
    }
}
