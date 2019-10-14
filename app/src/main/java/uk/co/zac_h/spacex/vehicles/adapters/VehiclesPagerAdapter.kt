package uk.co.zac_h.spacex.vehicles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.launches.LaunchesListFragment
import uk.co.zac_h.spacex.vehicles.rockets.RocketFragment

class VehiclesPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> RocketFragment()
            1 -> LaunchesListFragment.newInstance("past")
            else -> throw IllegalArgumentException("")
        }

    override fun getCount(): Int = 2

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Vehicles"
        1 -> "Capsules"
        else -> super.getPageTitle(position)
    }
}