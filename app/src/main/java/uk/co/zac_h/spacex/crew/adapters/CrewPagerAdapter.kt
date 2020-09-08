package uk.co.zac_h.spacex.crew.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import uk.co.zac_h.spacex.crew.details.CrewItemFragment
import uk.co.zac_h.spacex.model.spacex.CrewModel

class CrewPagerAdapter(fragment: FragmentManager, val crew: List<CrewModel>) :
    FragmentStatePagerAdapter(fragment, BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT) {

    override fun getItem(position: Int): Fragment = CrewItemFragment.newInstance(crew[position])

    override fun getCount(): Int = crew.size

}