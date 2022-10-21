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
import uk.co.zac_h.spacex.crew.Crew
import uk.co.zac_h.spacex.databinding.FragmentLaunchDetailsCrewBinding
import uk.co.zac_h.spacex.launches.LaunchesViewModel
import uk.co.zac_h.spacex.launches.adapters.LaunchCrewAdapter

class LaunchDetailsCrewFragment : BaseFragment() {

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

        crewAdapter = LaunchCrewAdapter { astronaut -> onClick(astronaut) }

        binding.launchDetailsCrewRecycler.apply {
            setHasFixedSize(false)
            adapter = crewAdapter
        }

        viewModel.launch?.crew?.let { update(it) }
    }

    fun update(response: List<Crew>) {
        crewAdapter.submitList(response)
        binding.launchDetailsCrewRecycler.scheduleLayoutAnimation()
    }

    private fun onClick(astronaut: Crew) {
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
