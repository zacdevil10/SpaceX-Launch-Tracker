package uk.co.zac_h.spacex.network.launchlibrary

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import uk.co.zac_h.spacex.network.dto.spacex.LaunchLibraryPaginatedResponse
import uk.co.zac_h.spacex.network.launchlibrary.model.AgencyEndpointDetailed
import uk.co.zac_h.spacex.network.launchlibrary.model.AstronautEndpointNormal
import uk.co.zac_h.spacex.network.launchlibrary.model.LaunchDetailed
import uk.co.zac_h.spacex.network.launchlibrary.model.LauncherDetailed
import uk.co.zac_h.spacex.network.launchlibrary.model.SpacecraftNormal
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
        val response = parse<LaunchLibraryPaginatedResponse<LaunchDetailed>>("upcoming")
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
        val response = parse<LaunchLibraryPaginatedResponse<LaunchDetailed>>("previous")
        assertTrue(response.results.isNotEmpty())
        assertNotNull(response.next)
        assertTrue(response.results.all { it.id != null && it.net != null })
    }

    @Test
    fun `launchers parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<LauncherDetailed>>("launchers")
        assertTrue(response.results.isNotEmpty())
        assertTrue(response.results.all { it.serialNumber != null })
        assertTrue(response.results.any { it.launcherConfig?.fullName != null })
    }

    @Test
    fun `spacecraft parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<SpacecraftNormal>>("spacecraft")
        assertTrue(response.results.isNotEmpty())
        assertTrue(response.results.all { it.name != null && it.status?.name != null })
    }

    @Test
    fun `astronauts parse`() {
        val response = parse<LaunchLibraryPaginatedResponse<AstronautEndpointNormal>>("astronauts")
        assertTrue(response.results.isNotEmpty())
        assertTrue(response.results.all { it.name != null && it.status?.name != null })
    }

    @Test
    fun `spacex agency parses with its launcher and spacecraft configs`() {
        val agency = parse<AgencyEndpointDetailed>("agency")
        assertEquals(121, agency.id)
        assertTrue(!agency.launcherList.isNullOrEmpty())
        assertTrue(!agency.spacecraftList.isNullOrEmpty())
    }
}
