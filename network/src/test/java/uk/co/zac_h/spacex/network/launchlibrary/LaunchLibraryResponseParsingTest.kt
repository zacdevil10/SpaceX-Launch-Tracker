package uk.co.zac_h.spacex.network.launchlibrary

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormalDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailedDto
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormalDto
import uk.co.zac_h.spacex.network.networkJson

/**
 * Parses real Launch Library 2.3.0 responses (src/test/resources/launchlibrary, captured from
 * lldev.thespacedevs.com with the same queries LaunchLibraryService sends) into the generated
 * models with the app's Json config. The schema claims some fields are never null when the API
 * does send null, so this catches serialization crashes that would otherwise only show at runtime.
 */
class LaunchLibraryResponseParsingTest {

    private inline fun <reified T> parse(name: String): T {
        val text = checkNotNull(javaClass.classLoader?.getResource("launchlibrary/$name.json")) {
            "Missing fixture $name.json"
        }.readText()
        return networkJson.decodeFromString<T>(text)
    }

    @Test
    fun `upcoming launches parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<LaunchDetailedDto>>("upcoming")
        assertTrue(response.results.isNotEmpty())
        response.results.forEach { launch ->
            assertNotNull(launch.id)
            assertNotNull(launch.name)
            assertNotNull(launch.status?.id)
        }
        assertTrue(response.results.any { !it.rocket?.launcherStage.isNullOrEmpty() })
    }

    @Test
    fun `previous launches parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<LaunchDetailedDto>>("previous")
        assertTrue(response.results.isNotEmpty())
        assertNotNull(response.next)
        assertTrue(response.results.all { it.id != null && it.net != null })
    }

    @Test
    fun `launchers parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<LauncherDetailedDto>>("launchers")
        assertTrue(response.results.isNotEmpty())
        assertTrue(response.results.all { it.serialNumber != null })
        assertTrue(response.results.any { it.launcherConfig?.fullName != null })
    }

    @Test
    fun `spacecraft parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<SpacecraftNormalDto>>("spacecraft")
        assertTrue(response.results.isNotEmpty())
        assertTrue(response.results.all { it.name != null && it.status?.name != null })
    }

    @Test
    fun `astronauts parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<AstronautEndpointNormalDto>>("astronauts")
        assertTrue(response.results.isNotEmpty())
        assertTrue(response.results.all { it.name != null && it.status?.name != null })
    }

    @Test
    fun `spacex agency parses with its launcher and spacecraft configs`() {
        val agency = parse<AgencyEndpointDetailedDto>("agency")
        assertEquals(121, agency.id)
        assertTrue(!agency.launcherList.isNullOrEmpty())
        assertTrue(!agency.spacecraftList.isNullOrEmpty())
    }
}
