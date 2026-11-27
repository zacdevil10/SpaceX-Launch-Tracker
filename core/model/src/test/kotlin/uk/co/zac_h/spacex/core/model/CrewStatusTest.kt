package uk.co.zac_h.spacex.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CrewStatusTest {

    @Test
    fun `maps every Launch Library status name`() {
        assertEquals(CrewStatus.IN_TRAINING, CrewStatus.fromApiName("In-Training"))
        assertEquals(CrewStatus.ACTIVE, CrewStatus.fromApiName("Active"))
        assertEquals(CrewStatus.INACTIVE, CrewStatus.fromApiName("Inactive"))
        assertEquals(CrewStatus.RETIRED, CrewStatus.fromApiName("Retired"))
        assertEquals(CrewStatus.DECEASED, CrewStatus.fromApiName("Deceased"))
        assertEquals(CrewStatus.LOST_IN_TRAINING, CrewStatus.fromApiName("Lost In Training"))
    }

    @Test
    fun `unknown or missing names are UNKNOWN`() {
        assertEquals(CrewStatus.UNKNOWN, CrewStatus.fromApiName("Lost"))
        assertEquals(CrewStatus.UNKNOWN, CrewStatus.fromApiName(null))
    }
}
