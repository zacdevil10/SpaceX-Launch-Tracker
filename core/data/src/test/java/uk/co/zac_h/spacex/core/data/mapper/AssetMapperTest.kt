package uk.co.zac_h.spacex.core.data.mapper

import kotlinx.serialization.json.Json
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test
import uk.co.zac_h.spacex.core.model.CrewStatus
import uk.co.zac_h.spacex.core.model.RocketFamily
import uk.co.zac_h.spacex.core.model.RocketType
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherConfigDetailedSerializerNoManufacturerDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftConfigDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto
import java.time.Instant
import java.time.LocalDate

class AssetMapperTest {

    // The same settings as the app's Launch Library client.
    private val json = Json {
        ignoreUnknownKeys = true
        explicitNulls = false
    }

    @Test
    fun `maps an astronaut`() {
        val astronaut = json.decodeFromString<AstronautEndpointNormalDto>(
            """
            {
              "id": 276,
              "name": "Robert Behnken",
              "status": {"id": 2, "name": "Retired"},
              "agency": {"id": 44, "name": "National Aeronautics and Space Administration"},
              "image": {"id": 9, "image_url": "https://example.com/behnken.jpg"},
              "nationality": [
                {"id": 1, "nationality_name": "American"},
                {"id": 2}
              ],
              "bio": "Test pilot.",
              "first_flight": "2008-03-11T06:28:14Z"
            }
            """
        ).toAstronaut()

        assertEquals(276, astronaut.id)
        assertEquals("Robert Behnken", astronaut.name)
        assertEquals(CrewStatus.RETIRED, astronaut.status)
        assertEquals("National Aeronautics and Space Administration", astronaut.agency)
        assertEquals(listOf("American"), astronaut.nationalities)
        assertEquals("Test pilot.", astronaut.bio)
        assertEquals("https://example.com/behnken.jpg", astronaut.imageUrl)
        assertEquals(Instant.parse("2008-03-11T06:28:14Z"), astronaut.firstFlight)
    }

    @Test
    fun `maps a rocket configuration and picks its main family`() {
        val rocket = json.decodeFromString<LauncherConfigDetailedSerializerNoManufacturerDto>(
            """
            {
              "id": 164,
              "name": "Falcon 9",
              "full_name": "Falcon 9 Block 5",
              "families": [{"id": 2, "name": "Falcon 9"}, {"id": 1, "name": "Falcon"}],
              "description": "Two stage rocket.",
              "image": {"id": 3, "image_url": "https://example.com/f9.jpg"},
              "max_stage": 2,
              "length": 70.0,
              "diameter": 3.65,
              "maiden_flight": "2018-05-11",
              "launch_mass": 549.0,
              "leo_capacity": 22800.0,
              "gto_capacity": 8300.0,
              "to_thrust": 7607.0,
              "consecutive_successful_launches": 300,
              "successful_launches": 400,
              "failed_launches": 0,
              "pending_launches": 50
            }
            """
        ).toRocket()

        assertEquals(164, rocket.id)
        assertEquals("Falcon 9 Block 5", rocket.fullName)
        assertEquals(RocketType.FALCON_NINE, rocket.type)
        assertEquals(RocketFamily.FALCON, rocket.family)
        assertEquals(2, rocket.maxStages)
        assertEquals(LocalDate.of(2018, 5, 11), rocket.maidenFlight)
        assertEquals(7607.0, rocket.takeoffThrust!!, 0.0)
        assertEquals(400, rocket.successfulLaunches)
        assertEquals("https://example.com/f9.jpg", rocket.imageUrl)
    }

    @Test
    fun `an unknown rocket has no type or family`() {
        val rocket = json.decodeFromString<LauncherConfigDetailedSerializerNoManufacturerDto>(
            """{"id": 1, "name": "Falcon 9 v1.0", "families": [{"id": 2, "name": "Falcon 9"}]}"""
        ).toRocket()

        assertNull(rocket.type)
        assertEquals(RocketFamily.NONE, rocket.family)
        assertNull(rocket.maidenFlight)
    }

    @Test
    fun `maps a booster`() {
        val launcher = json.decodeFromString<LauncherDetailedDto>(
            """
            {
              "id": 1062,
              "serial_number": "B1062",
              "status": {"id": 4, "name": "Lost"},
              "details": "",
              "launcher_config": {"id": 164, "name": "Falcon 9", "full_name": "Falcon 9 Block 5"},
              "image": {"id": 5, "image_url": "https://example.com/b1062.jpg"},
              "flights": 23,
              "first_launch_date": "2020-11-05T23:24:23Z",
              "last_launch_date": "2024-08-28T07:48:00Z"
            }
            """
        ).toLauncher()

        assertEquals(1062, launcher.id)
        assertEquals("B1062", launcher.serial)
        assertEquals("Lost", launcher.status)
        assertEquals("", launcher.details)
        assertEquals("Falcon 9 Block 5", launcher.configuration)
        assertEquals(23, launcher.flights)
        assertEquals(Instant.parse("2020-11-05T23:24:23Z"), launcher.firstLaunch)
        assertEquals(Instant.parse("2024-08-28T07:48:00Z"), launcher.lastLaunch)
    }

    @Test
    fun `maps a spacecraft`() {
        val spacecraft = json.decodeFromString<SpacecraftNormalDto>(
            """
            {
              "id": 2,
              "name": "Endeavour",
              "serial_number": "C206",
              "status": {"id": 1, "name": "Active"},
              "description": "The first Crew Dragon to fly crew.",
              "spacecraft_config": {"id": 7, "name": "Crew Dragon"},
              "image": {"id": 6, "image_url": "https://example.com/c206.jpg"}
            }
            """
        ).toSpacecraft()

        assertEquals(2, spacecraft.id)
        assertEquals("Endeavour", spacecraft.name)
        assertEquals("C206", spacecraft.serial)
        assertEquals("Active", spacecraft.status)
        assertEquals("Crew Dragon", spacecraft.configuration)
        assertEquals("https://example.com/c206.jpg", spacecraft.imageUrl)
    }

    @Test
    fun `maps a spacecraft configuration`() {
        val configuration = json.decodeFromString<SpacecraftConfigDetailedDto>(
            """
            {
              "id": 7,
              "name": "Crew Dragon",
              "in_use": true,
              "history": "Developed for Commercial Crew.",
              "details": "Carries up to seven.",
              "maiden_flight": "2019-03-02",
              "height": 8.1,
              "diameter": 4.0,
              "human_rated": true,
              "crew_capacity": 7,
              "payload_capacity": 6000,
              "wiki_link": "https://example.com/wiki",
              "info_link": "https://example.com/info"
            }
            """
        ).toSpacecraftConfiguration()

        assertEquals(7, configuration.id)
        assertEquals(true, configuration.inUse)
        assertEquals(LocalDate.of(2019, 3, 2), configuration.maidenFlight)
        assertEquals(8.1, configuration.height!!, 0.0)
        assertEquals(7, configuration.crewCapacity)
        assertEquals("https://example.com/wiki", configuration.wikiUrl)
        assertEquals("https://example.com/info", configuration.infoUrl)
    }
}
