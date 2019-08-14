package uk.co.zac_h.spacex.launches.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.launches.LaunchesListFragment

class LaunchesPagerAdapter(fragmentManager: FragmentManager) : FragmentPagerAdapter(fragmentManager) {

    override fun getItem(position: Int): Fragment? =
        when (position) {
            0 -> LaunchesListFragment.newInstance("upcoming")
            1 -> LaunchesListFragment.newInstance("past")
            else -> null
        }


    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Upcoming"
        1 -> "Past"
        else -> null
    }

}