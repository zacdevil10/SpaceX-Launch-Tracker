package uk.co.zac_h.spacex.feature.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
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

class VehiclesFragment : BaseFragment() {

    private lateinit var binding: FragmentViewPagerBinding

    private val viewModel: VehiclesFilterViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

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

        val fragments: List<ViewPagerFragment> = listOf(
            RocketFragment(),
            DragonFragment(),
            LauncherFragment(),
            //CapsulesFragment()
        )

        binding.tabLayout.tabMode = TabLayout.MODE_FIXED

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.viewPager.apply {
            adapter = ViewPagerAdapter(childFragmentManager, fragments)
            addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
                override fun onPageScrolled(p: Int, p1: Float, p2: Int) {}

                override fun onPageSelected(position: Int) {
                    viewModel.setVehiclesPage(position)

                    if (position == 2) {
                        binding.fab.hide()
                    } else {
                        binding.fab.show()
                    }
                }

                override fun onPageScrollStateChanged(state: Int) {}

            })
        }

        val tabIcons = listOf(
            R.drawable.ic_rocket,
            R.drawable.ic_dragon,
            R.drawable.ic_core,
            R.drawable.ic_dragon
        )

        binding.tabLayout.apply {
            setupWithViewPager(binding.viewPager)
            for (position in 0..tabCount) {
                getTabAt(position)?.setIcon(tabIcons[position])
            }
        }

        binding.fab.setOnClickListener {
            findNavController().navigate(VehiclesFragmentDirections.actionVehiclesPageToVehiclesFilter())
        }
    }
}
