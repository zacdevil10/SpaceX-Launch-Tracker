package uk.co.zac_h.spacex.launch.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import uk.co.zac_h.spacex.core.common.bottomsheet.BottomDrawerCallback
import uk.co.zac_h.spacex.core.common.bottomsheet.StandardBackPressedStateAction
import uk.co.zac_h.spacex.core.common.bottomsheet.StandardBottomSheetBackPressed
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.launch.CrewItem
import uk.co.zac_h.spacex.launch.LaunchesViewModel
import uk.co.zac_h.spacex.launch.R
import uk.co.zac_h.spacex.launch.adapters.LaunchCrewAdapter
import uk.co.zac_h.spacex.launch.databinding.FragmentLaunchDetailsCrewBinding

class LaunchDetailsCrewFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Crew"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsCrewBinding

    private lateinit var crewAdapter: LaunchCrewAdapter

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehaviour = from(binding.standardBottomSheet)
        bottomSheetBehaviour.state = STATE_COLLAPSED

        val back = StandardBottomSheetBackPressed(bottomSheetBehaviour)

        bottomSheetBehaviour.addBottomSheetCallback(BottomDrawerCallback().apply {
            addOnStateChangedAction(StandardBackPressedStateAction(back))
        })

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, back)

        crewAdapter = LaunchCrewAdapter { astronaut -> onClick(astronaut) }

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(false)
            adapter = crewAdapter
        }

        viewModel.launch?.crew?.let { update(it) }
    }

    fun update(response: List<CrewItem>) {
        crewAdapter.submitList(response)
        binding.launchDetailsCrewRecycler.scheduleLayoutAnimation()
    }

    private fun onClick(astronaut: CrewItem) {
        binding.apply {
            title.text = astronaut.name
            role.text = astronaut.role
            agency.text = astronaut.agency
            status.text = astronaut.status.status
            firstFlight.text = astronaut.firstFlight
            bio.text = astronaut.bio
        }

        bottomSheetBehaviour.state = STATE_EXPANDED
    }
}
