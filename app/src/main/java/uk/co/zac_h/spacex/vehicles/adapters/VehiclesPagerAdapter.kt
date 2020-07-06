package uk.co.zac_h.spacex.vehicles.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesFragment
import uk.co.zac_h.spacex.vehicles.cores.CoreFragment
import uk.co.zac_h.spacex.vehicles.dragon.DragonFragment
import uk.co.zac_h.spacex.vehicles.rockets.RocketFragment
import uk.co.zac_h.spacex.vehicles.ships.ShipsFragment

class VehiclesPagerAdapter(fragmentManager: FragmentManager) :
    FragmentPagerAdapter(fragmentManager, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment =
        when (position) {
            0 -> RocketFragment()
            1 -> DragonFragment()
            2 -> ShipsFragment()
            3 -> CoreFragment()
            4 -> CapsulesFragment()
            else -> throw IllegalArgumentException("Invalid position: $position")
        }

    override fun getCount(): Int = 5

    override fun getPageTitle(position: Int): CharSequence? = when (position) {
        0 -> "Rockets"
        1 -> "Dragon"
        2 -> "Ships"
        3 -> "Cores"
        4 -> "Capsules"
        else -> super.getPageTitle(position)
    }
}