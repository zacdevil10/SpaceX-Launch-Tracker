package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsFragment
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsFragment
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsFragment
import uk.co.zac_h.spacex.model.spacex.LaunchesExtendedModel

class LaunchDetailsContainerFragment : Fragment(), LaunchDetailsContainerContract.View {

    private var binding: FragmentLaunchDetailsContainerBinding? = null

    private var presenter: LaunchDetailsContainerContract.Presenter? = null

    private var selectedItem: Int? = null

    private var launchShort: LaunchesExtendedModel? = null
    private var id: String? = null

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        launchShort = arguments?.getParcelable("launch_short")
        id = arguments?.getString("launch_id")
        selectedItem = savedInstanceState?.getInt("selected_item")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentLaunchDetailsContainerBinding.inflate(inflater, container, false)
        return binding?.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        (activity as MainActivity).setSupportActionBar(binding?.toolbar)

        val navController = NavHostFragment.findNavController(this)
        val drawerLayout = requireActivity().findViewById<DrawerLayout>(R.id.drawer_layout)
        val appBarConfig =
            AppBarConfiguration.Builder((context?.applicationContext as App).startDestinations)
                .setOpenableLayout(drawerLayout).build()

        binding?.toolbar?.setupWithNavController(navController, appBarConfig)

        presenter = LaunchDetailsContainerPresenter(this)

        launchShort?.let {
            binding?.toolbar?.title = it.missionName
            binding?.fragmentLaunchDetailsContainer?.transitionName = it.id

            if (it.crew?.isNotEmpty() == true || it.ships?.isNotEmpty() == true) {
                if (it.crew?.isNotEmpty() == true && it.ships?.isNotEmpty() == true) {
                    binding?.launchDetailsBottomNavigation?.inflateMenu(R.menu.launch_details_bottom_nav_menu_all)
                } else if (it.crew?.isNotEmpty() == true) {
                    binding?.launchDetailsBottomNavigation?.inflateMenu(R.menu.launch_details_bottom_nav_menu_crew)
                } else if (it.ships?.isNotEmpty() == true) {
                    binding?.launchDetailsBottomNavigation?.inflateMenu(R.menu.launch_details_bottom_nav_menu_ships)
                }
            } else {
                binding?.launchDetailsBottomNavigation?.inflateMenu(R.menu.launch_details_bottom_nav_menu)
            }

            presenter?.startCountdown(it.launchDateUnix, it.tbd)
        } ?: id?.let {
            binding?.fragmentLaunchDetailsContainer?.transitionName = it

            binding?.launchDetailsBottomNavigation?.inflateMenu(R.menu.launch_details_bottom_nav_menu)
        }

        if (selectedItem == null) {
            launchShort?.let {
                replaceFragment(LaunchDetailsFragment.newInstance(it))
            } ?: id?.let {
                replaceFragment(LaunchDetailsFragment.newInstance(it))
            }
        } else {
            binding?.launchDetailsBottomNavigation?.selectedItemId = selectedItem as Int
        }

        binding?.launchDetailsBottomNavigation?.setOnNavigationItemSelectedListener {
            if (selectedItem != it.itemId) {
                selectedItem = it.itemId
                when (it.itemId) {
                    R.id.launch_details_details -> {
                        launchShort?.let { launchShort ->
                            replaceFragment(LaunchDetailsFragment.newInstance(launchShort))
                        } ?: id?.let { id ->
                            replaceFragment(LaunchDetailsFragment.newInstance(id))
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.launch_details_cores -> {
                        launchShort?.let { launchShort ->
                            replaceFragment(LaunchDetailsCoresFragment.newInstance(launchShort.id))
                        } ?: id?.let { id ->
                            replaceFragment(LaunchDetailsCoresFragment.newInstance(id))
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.launch_details_payloads -> {
                        launchShort?.let { launchShort ->
                            replaceFragment(LaunchDetailsPayloadsFragment.newInstance(launchShort.id))
                        } ?: id?.let { id ->
                            replaceFragment(LaunchDetailsPayloadsFragment.newInstance(id))
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.launch_details_crew -> {
                        launchShort?.let { launchShort ->
                            replaceFragment(LaunchDetailsCrewFragment.newInstance(launchShort.id))
                        } ?: id?.let { id ->
                            replaceFragment(LaunchDetailsCrewFragment.newInstance(id))
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                    R.id.launch_details_ships -> {
                        launchShort?.let { launchShort ->
                            replaceFragment(LaunchDetailsShipsFragment.newInstance(launchShort.id))
                        } ?: id?.let { id ->
                            replaceFragment(LaunchDetailsShipsFragment.newInstance(id))
                        }
                        return@setOnNavigationItemSelectedListener true
                    }
                }
            }
            false
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        selectedItem?.let { outState.putInt("selected_item", it) }
        super.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        countdownTimer?.cancel()
        countdownTimer = null
        binding = null
    }

    override fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {

            }
        }

        countdownTimer?.start()
    }

    override fun updateCountdown(countdown: String) {
        binding?.launchDetailsCountdownText?.text = countdown
    }

    private fun replaceFragment(fragment: Fragment) {
        childFragmentManager
            .beginTransaction()
            .replace(
                R.id.launch_details_fragment,
                fragment
            )
            .commit()
    }

    override fun showCountdown() {
        binding?.launchDetailsCountdownText?.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding?.launchDetailsCountdownText?.visibility = View.GONE
    }
}