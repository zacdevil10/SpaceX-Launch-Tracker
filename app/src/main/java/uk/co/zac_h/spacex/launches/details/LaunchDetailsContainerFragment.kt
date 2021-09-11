package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.dto.spacex.Launch
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsFragment
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsFragment
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsFragment

class LaunchDetailsContainerFragment : BaseFragment() {

    override val title: String by lazy { navArgs.label ?: title }

    private val navArgs: LaunchDetailsContainerFragmentArgs by navArgs()

    private val viewModel: LaunchDetailsContainerViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsContainerBinding

    private var selectedItem: Int? = null

    private var countdownTimer: CountDownTimer? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()

        viewModel.launchID = navArgs.launchId

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

        binding.fragmentLaunchDetailsContainer.transitionName = navArgs.launchId

        viewModel.getLaunch()

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
        binding.toolbarLayout.toolbar.setup()

        viewModel.launch.observe(viewLifecycleOwner) { response ->
            when (response.status) {
                ApiResult.Status.PENDING -> {
                }
                ApiResult.Status.SUCCESS -> response.data?.let { update(it) }
                ApiResult.Status.FAILURE -> showError(response.error?.message)
            }
        }

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
        if (launch.crew?.isNotEmpty() == true || launch.ships?.isNotEmpty() == true) {
            if (launch.crew?.isNotEmpty() == true && launch.ships?.isNotEmpty() == true) {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_all)
            } else if (launch.crew?.isNotEmpty() == true) {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_crew)
            } else if (launch.ships?.isNotEmpty() == true) {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_ships)
            }
        } else {
            binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu)
        }

        //presenter?.startCountdown(launch.launchDate?.dateUnix, launch.tbd)

        if (selectedItem == null) {
            replaceFragment(LaunchDetailsFragment())
        } else {
            binding.launchDetailsBottomNavigation.selectedItemId = selectedItem as Int
        }
    }

    fun setCountdown(time: Long) {
        countdownTimer?.cancel()
        countdownTimer = object : CountDownTimer(time, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                //presenter?.updateCountdown(millisUntilFinished)
            }

            override fun onFinish() {
                /*launchShort?.links?.webcast?.let { link ->
                    binding.launchDetailsCountdown.visibility = View.GONE
                    binding.launchDetailsWatchNow.apply {
                        visibility = View.VISIBLE
                        setOnClickListener {
                            openWebLink(link)
                        }
                    }
                }*/
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

    fun showCountdown() {
        binding.launchDetailsCountdown.visibility = View.VISIBLE
    }

    fun hideCountdown() {
        binding.launchDetailsCountdown.visibility = View.GONE
    }

    private fun showError(error: String?) {

    }
}