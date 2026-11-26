package uk.co.zac_h.spacex

import org.junit.Assert.assertEquals
import org.junit.Test

class TopLevelNavigationTest {

    @Test
    fun `the bar holds the primary destinations then More`() {
        assertEquals(
            listOf(
                TopLevelNavigation.Launches,
                TopLevelNavigation.News,
                TopLevelNavigation.Assets,
                TopLevelNavigation.More
            ),
            TopLevelNavigation.barDestinations
        )
    }

    @Test
    fun `the More sheet lists Starlink, Company and Settings`() {
        assertEquals(
            listOf(TopLevelNavigation.Starlink, TopLevelNavigation.Company, TopLevelNavigation.Settings),
            TopLevelNavigation.moreDestinations
        )
    }

    @Test
    fun `More and Settings have no back stack of their own`() {
        assertEquals(
            listOf(
                TopLevelNavigation.Launches,
                TopLevelNavigation.News,
                TopLevelNavigation.Assets,
                TopLevelNavigation.Starlink,
                TopLevelNavigation.Company
            ),
            TopLevelNavigation.backStackDestinations
        )
    }

    @Test
    fun `More is highlighted in the bar while a destination from the sheet shows`() {
        assertEquals(TopLevelNavigation.More, TopLevelNavigation.Company.barDestination)
        assertEquals(TopLevelNavigation.More, TopLevelNavigation.Starlink.barDestination)
        assertEquals(TopLevelNavigation.Assets, TopLevelNavigation.Assets.barDestination)
    }

    @Test
    fun `the destination showing is selected`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.Company,
            backStackTop = CompanyKey
        )

        assertEquals(TopLevelNavigation.Company, selected)
    }

    @Test
    fun `the Settings dialog is selected over any destination`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.Launches,
            backStackTop = SettingsKey
        )

        assertEquals(TopLevelNavigation.Settings, selected)
    }
}
