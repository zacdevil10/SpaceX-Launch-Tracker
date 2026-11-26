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
    fun `More lists Company and Settings`() {
        assertEquals(
            listOf(TopLevelNavigation.Company, TopLevelNavigation.Settings),
            TopLevelNavigation.moreDestinations
        )
    }

    @Test
    fun `a primary destination is selected as itself`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.News,
            backStackTop = NewsKey,
            moreTop = MoreKey
        )

        assertEquals(TopLevelNavigation.News, selected)
    }

    @Test
    fun `the More list on its own selects More`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.More,
            backStackTop = MoreKey,
            moreTop = MoreKey
        )

        assertEquals(TopLevelNavigation.More, selected)
    }

    @Test
    fun `Company open on More selects Company`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.More,
            backStackTop = CompanyKey,
            moreTop = CompanyKey
        )

        assertEquals(TopLevelNavigation.Company, selected)
    }

    @Test
    fun `Company left open on More isn't selected while another tab shows`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.Assets,
            backStackTop = AssetsKey,
            moreTop = CompanyKey
        )

        assertEquals(TopLevelNavigation.Assets, selected)
    }

    @Test
    fun `the Settings dialog is selected over any destination`() {
        val selected = TopLevelNavigation.selected(
            route = TopLevelNavigation.Launches,
            backStackTop = SettingsKey,
            moreTop = MoreKey
        )

        assertEquals(TopLevelNavigation.Settings, selected)
    }
}
