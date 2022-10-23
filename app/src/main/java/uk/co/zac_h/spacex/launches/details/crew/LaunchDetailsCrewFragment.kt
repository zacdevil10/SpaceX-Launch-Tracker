package uk.co.zac_h.spacex.launches.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.*
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.launches.CrewItem
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.launches.adapters.LaunchCrewAdapter
import uk.co.zac_h.spacex.utils.BottomDrawerCallback
import uk.co.zac_h.spacex.utils.StandardBackPressedStateAction
import uk.co.zac_h.spacex.utils.StandardBottomSheetBackPressed

class LaunchDetailsCrewFragment : BaseFragment() {

    override var title: String = "Crew"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.nav_graph) {
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
