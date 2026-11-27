package uk.co.zac_h.spacex.core.model

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class LaunchStatusTest {

    private fun status(id: Int?) = LaunchStatus(id, name = null, abbreviation = null, description = null)

    @Test
    fun `go, TBD, hold, in flight and TBC are upcoming`() {
        listOf(1, 2, 5, 6, 8).forEach { assertTrue("status $it", status(it).isUpcoming) }
    }

    @Test
    fun `success, failure and partial failure are not upcoming`() {
        listOf(3, 4, 7).forEach { assertFalse("status $it", status(it).isUpcoming) }
        assertFalse(status(null).isUpcoming)
    }
}
