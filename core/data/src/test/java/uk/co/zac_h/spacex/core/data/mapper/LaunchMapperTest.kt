package uk.co.zac_h.spacex.core.data.mapper

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.co.zac_h.spacex.core.model.CoreType
import uk.co.zac_h.spacex.core.model.CrewStatus
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import java.time.Duration
import java.time.Instant

class LaunchMapperTest {

    // The same settings as the app's Launch Library client.
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    private fun fixture(): LaunchDetailedDto {
        val text = checkNotNull(javaClass.classLoader?.getResource("launch.json")).readText()
        return json.decodeFromString(text)
    }

    @Test
    fun `maps the launch's own fields`() {
        val launch = fixture().toLaunch()

        assertEquals("b0d27def-0000-4000-8000-000000000001", launch.id)
        assertEquals("Crew-12", launch.name)
        assertEquals(Instant.parse("2026-11-29T04:39:00Z"), launch.net)
        assertEquals(Instant.parse("2026-11-21T20:29:25Z"), launch.lastUpdated)
        assertEquals(1, launch.status.id)
        assertEquals("Go", launch.status.abbreviation)
        assertTrue(launch.isUpcoming)
        assertEquals("Falcon 9", launch.rocket)
        assertEquals("https://example.com/patch.png", launch.missionPatchUrl)
        assertEquals("Space Launch Complex 40", launch.pad?.name)
        assertEquals("Low Earth Orbit", launch.mission?.orbit)
        assertTrue(launch.webcastLive)
    }

    @Test
    fun `keeps only webcasts with a link`() {
        val webcasts = fixture().toLaunch().webcasts

        assertEquals(1, webcasts.size)
        with(webcasts.single()) {
            assertEquals("https://www.youtube.com/watch?v=abc", url)
            assertEquals("Official Webcast", type)
            assertEquals("youtube.com", source)
            assertEquals(10, priority)
        }
    }

    @Test
    fun `maps first stages with their landing and turnaround`() {
        val stage = fixture().toLaunch().firstStages.single()

        assertEquals(1046, stage.id)
        assertEquals("B1081", stage.serial)
        assertEquals(CoreType.CORE, stage.type)
        assertTrue(stage.reused)
        assertEquals(29, stage.totalFlights)
        assertEquals("Falcon 9 Block 5 | USSF-481", stage.previousFlight)
        assertEquals(Duration.parse("P39DT15H53M55S"), stage.turnAroundTime)
        assertEquals("ASDS", stage.landing?.type)
        assertEquals("OCISLY", stage.landing?.location)
        assertEquals("Of Course I Still Love You", stage.landing?.locationName)
    }

    @Test
    fun `maps crew from every spacecraft stage`() {
        val member = fixture().toLaunch().crew.single()

        assertEquals(501, member.id)
        assertEquals("Commander", member.role)
        assertEquals("Jane Doe", member.astronaut.name)
        assertEquals(CrewStatus.ACTIVE, member.astronaut.status)
        assertEquals(listOf("American"), member.astronaut.nationalities)
        assertEquals(Instant.parse("2024-03-04T03:53:38Z"), member.astronaut.firstFlight)
    }

    @Test
    fun `a launch with no mission uses its own name and has no net when it is malformed`() {
        val launch = LaunchDetailedDto(id = "x", name = "Falcon 9 | Test", net = "soon").toLaunch()

        assertEquals("Falcon 9 | Test", launch.name)
        assertNull(launch.net)
        assertTrue(launch.webcasts.isEmpty())
        assertTrue(launch.firstStages.isEmpty())
        assertTrue(launch.crew.isEmpty())
    }
}
