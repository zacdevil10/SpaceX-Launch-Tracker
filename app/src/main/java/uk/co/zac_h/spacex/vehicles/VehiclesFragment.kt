package uk.co.zac_h.spacex.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.Hold
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.FragmentVehiclesBinding
import uk.co.zac_h.spacex.vehicles.adapters.VehiclesPagerAdapter

class VehiclesFragment : Fragment() {

    private var _binding: FragmentVehiclesBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = Hold()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentVehiclesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setDrawerLayout(drawerLayout).build()

        binding.toolbar.setupWithNavController(navController, appBarConfig)

        binding.toolbar.title = resources.getString(R.string.menu_vehicles)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.vehiclesViewPager.apply {
            adapter = VehiclesPagerAdapter(childFragmentManager)
            offscreenPageLimit = 3
        }
        binding.vehiclesTabLayout.setupWithViewPager(binding.vehiclesViewPager)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
