package uk.co.zac_h.spacex.launches.details

import android.os.Bundle
import android.os.CountDownTimer
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.navigation.navGraphViewModels
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.core.utils.*
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsContainerBinding
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.launches.details.cores.LaunchDetailsCoresFragment
import uk.co.zac_h.spacex.launches.details.crew.LaunchDetailsCrewFragment
import uk.co.zac_h.spacex.launches.details.details.LaunchDetailsFragment

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

        viewModel.launch?.let { update() }

        binding.launchDetailsContainerTabs.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (selectedItem != checkedId) {
                selectedItem = checkedId
                when (checkedId) {
                    R.id.details -> replaceFragment(LaunchDetailsFragment())
                    R.id.cores -> replaceFragment(LaunchDetailsCoresFragment())
                    R.id.crew -> replaceFragment(LaunchDetailsCrewFragment())
                }
            }
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

    private fun update() {
        if (selectedItem == null) {
            replaceFragment(LaunchDetailsFragment())
        }
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
