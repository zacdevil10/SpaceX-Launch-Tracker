package uk.co.zac_h.spacex.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.activityViewModels
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.Hold
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerAdapter
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.databinding.FragmentVehiclesBinding
import uk.co.zac_h.spacex.vehicles.dragon.DragonFragment
import uk.co.zac_h.spacex.vehicles.rockets.RocketFragment

class VehiclesFragment : BaseFragment(), ViewPagerFragment {

    override val title by lazy { "Vehicles" }

    private lateinit var binding: FragmentVehiclesBinding

    private val viewModel: VehiclesFilterViewModel by activityViewModels()

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

        val fragments: List<ViewPagerFragment> = listOf(
            RocketFragment(),
            DragonFragment(),
            //ShipsFragment(),
            //CoreFragment(),
            //CapsulesFragment()
        )

        binding.tabLayout.tabMode = TabLayout.MODE_FIXED

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.vehiclesViewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, fragments)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(p: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(position: Int) {
                    viewModel.setVehiclesPage(position)
                }

                override fun onPageScrollStateChanged(state: Int) {}

            })
        }

        val tabIcons = listOf(
            R.drawable.ic_rocket,
            R.drawable.ic_dragon,
            //R.drawable.ic_baseline_directions_boat_24,
            //R.drawable.ic_core,
            //R.drawable.ic_dragon
        )

        binding.tabLayout.apply {
            setupWithViewPager(binding.vehiclesViewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }
    }
}
