package uk.co.zac_h.spacex.feature.launch.details.crew

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetBehavior.from
import uk.co.zac_h.spacex.core.common.bottomsheet.AlphaSlideAction
import uk.co.zac_h.spacex.core.common.bottomsheet.BackPressedStateAction
import uk.co.zac_h.spacex.core.common.bottomsheet.BottomDrawerCallback
import uk.co.zac_h.spacex.core.common.bottomsheet.BottomSheetBackPressed
import uk.co.zac_h.spacex.core.common.bottomsheet.BottomSheetOpenable
import uk.co.zac_h.spacex.core.common.bottomsheet.VisibilityStateAction
import uk.co.zac_h.spacex.core.common.fragment.BaseFragment
import uk.co.zac_h.spacex.core.common.viewpager.ViewPagerFragment
import uk.co.zac_h.spacex.feature.launch.CrewItem
import uk.co.zac_h.spacex.feature.launch.LaunchesViewModel
import uk.co.zac_h.spacex.feature.launch.R
import uk.co.zac_h.spacex.feature.launch.adapters.LaunchCrewAdapter
import uk.co.zac_h.spacex.feature.launch.databinding.FragmentLaunchDetailsCrewBinding

class LaunchDetailsCrewFragment : BaseFragment(), ViewPagerFragment {

    override var title: String = "Crew"

    private val viewModel: LaunchesViewModel by navGraphViewModels(R.id.launch_nav_graph) {
        defaultViewModelProviderFactory
    }

    private lateinit var binding: FragmentLaunchDetailsCrewBinding

    private lateinit var crewAdapter: LaunchCrewAdapter

    private lateinit var bottomSheetBehaviour: BottomSheetBehavior<ConstraintLayout>

    private lateinit var openable: BottomSheetOpenable<ConstraintLayout>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchDetailsCrewBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        bottomSheetBehaviour = from(binding.standardBottomSheet)

        openable = BottomSheetOpenable(bottomSheetBehaviour)
        val closeOnBackPressed = BottomSheetBackPressed(bottomSheetBehaviour)

        bottomSheetBehaviour.apply {
            openable.close()

            addBottomSheetCallback(BottomDrawerCallback().apply {
                addOnSlideAction(AlphaSlideAction(binding.scrim))
                addOnStateChangedAction(VisibilityStateAction(binding.scrim))
                addOnStateChangedAction(BackPressedStateAction(closeOnBackPressed))
            })

            binding.scrim.setOnClickListener {
                openable.close()
            }
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            closeOnBackPressed
        )

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

        openable.open()
    }
}
