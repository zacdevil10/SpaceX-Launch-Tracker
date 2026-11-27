package uk.co.zac_h.spacex.core.model

import org.junit.Assert.assertEquals
import org.junit.Test

class CoreTypeTest {

    @Test
    fun `maps core and strap-on booster`() {
        assertEquals(CoreType.CORE, CoreType.fromApiName("Core"))
        assertEquals(CoreType.BOOSTER, CoreType.fromApiName("Strap-On Booster"))
    }

    @Test
    fun `anything else is OTHER`() {
        assertEquals(CoreType.OTHER, CoreType.fromApiName("Other"))
        assertEquals(CoreType.OTHER, CoreType.fromApiName("Side Booster"))
        assertEquals(CoreType.OTHER, CoreType.fromApiName(null))
    }
}
