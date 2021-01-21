package uk.co.zac_h.spacex.crew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import uk.co.zac_h.spacex.crew.details.CrewItemFragment
import uk.co.zac_h.spacex.model.spacex.Crew

class CrewPagerAdapter(fragment: FragmentManager, lifecycle: Lifecycle, val crew: List<Fragment>) :
    FragmentStateAdapter(fragment, lifecycle) {

    override fun createFragment(position: Int): Fragment = crew[position]

    override fun getItemCount(): Int = crew.size

}