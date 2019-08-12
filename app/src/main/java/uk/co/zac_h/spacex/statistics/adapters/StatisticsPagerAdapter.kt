package uk.co.zac_h.spacex.statistics.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.statistics.graphs.launchhistory.LaunchHistoryFragment
import uk.co.zac_h.spacex.statistics.graphs.launchrate.LaunchRateFragment

class StatisticsPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? =
        when (position) {
            0 -> LaunchHistoryFragment()
            1 -> LaunchRateFragment()
            2 -> LaunchHistoryFragment()
            3 -> LaunchHistoryFragment()
            4 -> LaunchHistoryFragment()
            5 -> LaunchHistoryFragment()
            6 -> LaunchHistoryFragment()
            else -> null
        }

    override fun getCount(): Int = 7

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Launch History"
        1 -> "Launch Rate"
        2 -> "Another thing"
        3 -> "How many more?"
        4 -> "One more?"
        5 -> "No, two?"
        6 -> "Lets make it 3"
        else -> null
    }
}