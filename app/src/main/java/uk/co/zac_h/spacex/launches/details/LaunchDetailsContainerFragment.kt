package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.findNavController
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.launches.Launch
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsFragment
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsFragment
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsFragment
import uk.co.zac_h.spacex.utils.*

class LaunchDetailsContainerFragment : BaseFragment() {

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsContainerBinding

    private var selectedItem: Int? = null

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform().apply {
            drawingViewId = R.id.nav_host
        }

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)

        selectedItem = savedInstanceState?.getInt("selected_item")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsContainerBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.fragmentLaunchDetailsContainer.transitionName = viewModel.launch?.id

        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }

        viewModel.launch?.let { update(it) }

        binding.launchDetailsBottomNavigation.setOnItemSelectedListener {
            if (selectedItem != it.itemId) {
                selectedItem = it.itemId
                when (it.itemId) {
                    R.id.launch_details_details -> replaceFragment(LaunchDetailsFragment())
                    R.id.launch_details_cores -> replaceFragment(LaunchDetailsCoresFragment())
                    R.id.launch_details_payloads -> replaceFragment(LaunchDetailsPayloadsFragment())
                    R.id.launch_details_crew -> replaceFragment(LaunchDetailsCrewFragment())
                    R.id.launch_details_ships -> replaceFragment(LaunchDetailsShipsFragment())
                }
            }
            true
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
    }

    private fun update(launch: Launch) {
        binding.launchDetailsBottomNavigation.menu.clear()
        if (launch.crew.isNullOrEmpty().not() || launch.ships.isNullOrEmpty().not()) {
            if (launch.crew.isNullOrEmpty().not() && launch.ships.isNullOrEmpty().not()) {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_all)
            } else if (launch.crew.isNullOrEmpty().not()) {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_crew)
            } else if (launch.ships.isNullOrEmpty().not()) {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_ships)
            }
        } else {
            binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu)
        }

        val dateUnix = launch.launchDate?.dateUtc?.toMillis()
        val time = dateUnix?.minus(System.currentTimeMillis()) ?: 0
        if (time >= 0) {
            setCountdown(launch, time)
            binding.launchDetailsCountdown.visibility = View.VISIBLE
        }

        if (selectedItem == null) {
            replaceFragment(LaunchDetailsFragment())
        } else {
            binding.launchDetailsBottomNavigation.selectedItemId = selectedItem as Int
        }
    }

    private fun setCountdown(launch: Launch, time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(time: Long) {
                updateCountdown(
                    String.format(
                        "T-%02d:%02d:%02d:%02d",
                        time.toCountdownDays(),
                        time.toCountdownHours(),
                        time.toCountdownMinutes(),
                        time.toCountdownSeconds()
                    )
                )
            }

            override fun onFinish() {
                launch.links?.webcast?.let { link ->
                    binding.launchDetailsCountdown.visibility = View.GONE
                    binding.launchDetailsWatchNow.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            openWebLink(link)
                        }
                    }
                }
            }
        }

        countdownTimer?.start()
    }

    fun updateCountdown(countdown: String) {
        binding.launchDetailsCountdown.text = countdown
    }

    private fun replaceFragment(fragment: Fragment): Boolean {
        childFragmentManager.commit {
            setCustomAnimations(
                R.anim.fade_in,
                R.anim.fade_out
            )
            replace(R.id.launch_details_fragment, fragment)
        }
        return true
    }
}
