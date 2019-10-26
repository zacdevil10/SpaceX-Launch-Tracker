package uk.co.zac_h.spacex.vehicles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesFragment
import uk.co.zac_h.spacex.vehicles.cores.CoreFragment
import uk.co.zac_h.spacex.vehicles.rockets.RocketFragment

class VehiclesPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> RocketFragment()
            1 -> CoreFragment()
            2 -> CapsulesFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    override fun getCount(): Int = 3

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Rockets"
        1 -> "Cores"
        2 -> "Capsules"
        else -> super.getPageTitle(position)
    }
}