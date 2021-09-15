package uk.co.zac_h.spacex.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.Hold
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentVehiclesBinding
import uk.co.zac_h.spacex.utils.ViewPagerAdapter
import uk.co.zac_h.spacex.vehicles.capsules.CapsulesFragment
import uk.co.zac_h.spacex.vehicles.cores.CoreFragment
import uk.co.zac_h.spacex.vehicles.dragon.DragonFragment
import uk.co.zac_h.spacex.vehicles.rockets.RocketFragment
import uk.co.zac_h.spacex.vehicles.ships.ShipsFragment

class VehiclesFragment : BaseFragment() {

    override val title by lazy { getString(R.string.vehicles_label) }

    private lateinit var binding: FragmentVehiclesBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVehiclesBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val fragments: List<BaseFragment> = listOf(
            RocketFragment(),
            DragonFragment(),
            ShipsFragment(),
            CoreFragment(),
            CapsulesFragment()
        )

        binding.tabLayout.tabMode = TabLayout.MODE_SCROLLABLE

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.vehiclesViewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, fragments)
        }

        val tabIcons = listOf(
            R.drawable.ic_rocket,
            R.drawable.ic_dragon,
            R.drawable.ic_baseline_directions_boat_24,
            R.drawable.ic_core,
            R.drawable.ic_dragon
        )

        binding.tabLayout.apply {
            setupWithViewPager(binding.vehiclesViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }
    }
}
