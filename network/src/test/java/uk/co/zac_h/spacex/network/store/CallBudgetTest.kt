package uk.co.zac_h.spacex.network.store

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.minutes

class CallBudgetTest {

    private var now = 0L
    private val budget = CallBudget { now }

    @Test
    fun `counts calls in the last hour`() {
        repeat(3) { budget.record() }

        assertEquals(12, budget.remaining())
    }

    @Test
    fun `calls older than an hour no longer count`() {
        repeat(3) { budget.record() }
        now += 30.minutes.inWholeMilliseconds
        budget.record()
        now += 30.minutes.inWholeMilliseconds

        assertEquals(14, budget.remaining())
        now += 1.hours.inWholeMilliseconds
        assertEquals(15, budget.remaining())
    }

    @Test
    fun `automatic refresh keeps the last calls for screens`() {
        repeat(CallBudget.LIMIT - CallBudget.RESERVED_FOR_SCREENS - 1) { budget.record() }
        assertTrue(budget.allowsAutomaticRefresh())

        budget.record()
        assertFalse(budget.allowsAutomaticRefresh())
    }
}
