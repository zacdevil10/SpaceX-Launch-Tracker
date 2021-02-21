package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.base.MainActivity
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsFragment
import uk.co.zac_h.spacex.launches.details.payloads.LaunchDetailsPayloadsFragment
import uk.co.zac_h.spacex.launches.details.ships.LaunchDetailsShipsFragment
import uk.co.zac_h.spacex.model.spacex.Launch

class LaunchDetailsContainerFragment : BaseFragment(), LaunchDetailsContainerContract.View {

    override val title: String by lazy { launchShort?.missionName ?: "" }

    private var _binding: FragmentLaunchDetailsContainerBinding? = null
    private val binding get() = _binding!!

    private var presenter: LaunchDetailsContainerContract.Presenter? = null

    private var selectedItem: Int? = null

    private var launchShort: Launch? = null
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
    ): View = FragmentLaunchDetailsContainerBinding.inflate(inflater, container, false).apply {
        _binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        postponeEnterTransition()
        view.doOnPreDraw { startPostponedEnterTransition() }

        binding.toolbarLayout.progress.hide()

        (activity as MainActivity).setSupportActionBar(binding.toolbarLayout.toolbar)
        binding.toolbarLayout.toolbar.setup()

        presenter = LaunchDetailsContainerPresenter(this)

        launchShort?.let {
            binding.fragmentLaunchDetailsContainer.transitionName = it.id

            if (it.crew?.isNotEmpty() == true || it.ships?.isNotEmpty() == true) {
                if (it.crew?.isNotEmpty() == true && it.ships?.isNotEmpty() == true) {
                    binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_all)
                } else if (it.crew?.isNotEmpty() == true) {
                    binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_crew)
                } else if (it.ships?.isNotEmpty() == true) {
                    binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu_ships)
                }
            } else {
                binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu)
            }

            presenter?.startCountdown(it.launchDate?.dateUnix, it.tbd)
        } ?: id?.let {
            binding.fragmentLaunchDetailsContainer.transitionName = it

            binding.launchDetailsBottomNavigation.inflateMenu(R.menu.launch_details_bottom_nav_menu)
        }

        if (selectedItem == null) {
            launchShort?.let {
                replaceFragment(LaunchDetailsFragment.newInstance(it))
            } ?: id?.let {
                replaceFragment(LaunchDetailsFragment.newInstance(it))
            }
        } else {
            binding.launchDetailsBottomNavigation.selectedItemId = selectedItem as Int
        }

        binding.launchDetailsBottomNavigation.setOnNavigationItemSelectedListener {
            if (selectedItem != it.itemId) {
                selectedItem = it.itemId
                when (it.itemId) {
                    R.id.launch_details_details -> {
                        replaceFragment(
                            LaunchDetailsFragment.newInstance(
                                launchShort ?: id ?: throw IllegalArgumentException()
                            )
                        )
                    }
                    R.id.launch_details_cores -> {
                        replaceFragment(
                            LaunchDetailsCoresFragment.newInstance(
                                launchShort?.id ?: id ?: throw IllegalArgumentException()
                            )
                        )
                    }
                    R.id.launch_details_payloads -> {
                        replaceFragment(
                            LaunchDetailsPayloadsFragment.newInstance(
                                launchShort?.id ?: id ?: throw IllegalArgumentException()
                            )
                        )
                    }
                    R.id.launch_details_crew -> {
                        replaceFragment(
                            LaunchDetailsCrewFragment.newInstance(
                                launchShort?.id ?: id ?: throw IllegalArgumentException()
                            )
                        )
                    }
                    R.id.launch_details_ships -> {
                        replaceFragment(
                            LaunchDetailsShipsFragment.newInstance(
                                launchShort?.id ?: id ?: throw IllegalArgumentException()
                            )
                        )
                    }
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
        _binding = null
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
        binding.launchDetailsCountdownText.text = countdown
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

    override fun showCountdown() {
        binding.launchDetailsCountdownText.visibility = View.VISIBLE
    }

    override fun hideCountdown() {
        binding.launchDetailsCountdownText.visibility = View.GONE
    }
}