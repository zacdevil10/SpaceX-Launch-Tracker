package uk.co.zac_h.spacex.feature.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import com.google.android.material.transition.MaterialSharedAxis
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerAdapter
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.core.ui.databinding.FragmentViewPagerBinding
import uk.co.zac_h.spacex.feature.vehicles.dragon.DragonFragment
import uk.co.zac_h.spacex.feature.vehicles.launcher.LauncherFragment
import uk.co.zac_h.spacex.feature.vehicles.rockets.RocketFragment
import uk.co.zac_h.spacex.feature.vehicles.spacecraft.SpacecraftFragment

class VehiclesFragment : BaseFragment() {

    private lateinit var binding: FragmentViewPagerBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enterTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
        exitTransition = MaterialSharedAxis(MaterialSharedAxis.X, true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentViewPagerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.doOnPreDraw { startPostponedEnterTransition() }
        postponeEnterTransition()

        val fragments: List<ViewPagerFragment> = listOf(
            RocketFragment(),
            DragonFragment(),
            LauncherFragment(),
            SpacecraftFragment()
        )

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, fragments)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(p: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(position: Int) {
                    when (position) {
                        0 -> binding.fab.show()
                        1, 2, 3 -> binding.fab.hide()
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}

            })
        }

        binding.tabLayout.apply {
            tabMode = TabLayout.MODE_AUTO
            setupWithViewPager(binding.viewPager)
        }

        binding.fab.setOnClickListener {
            when (binding.viewPager.currentItem) {
                0 -> findNavController().navigate(VehiclesFragmentDirections.actionVehiclesToRocketFilter())
            }
        }
    }
}
