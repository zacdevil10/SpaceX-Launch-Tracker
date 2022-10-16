package uk.co.zac_h.spacex.crew.details

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.crew.Crew
import uk.co.zac_h.spacex.crew.CrewViewModel
import uk.co.zac_h.spacex.crew.adapters.CrewMissionsAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewItemBinding
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_ACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_INACTIVE
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_RETIRED
import uk.co.zac_h.spacex.network.SPACEX_CREW_STATUS_UNKNOWN
import uk.co.zac_h.spacex.network.dto.spacex.CrewStatus

class CrewItemFragment : BaseFragment() {

    private lateinit var binding: FragmentCrewItemBinding

    private val viewModel: CrewViewModel by navGraphViewModels(R.id.nav_graph) {
        defaultViewModelProviderFactory
    }

    private val navArgs: CrewItemFragmentArgs by navArgs()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sharedElementEnterTransition = MaterialContainerTransform()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentCrewItemBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val bottomSheetBehavior = BottomSheetBehavior.from(binding.crewBottomSheet)
        bottomSheetBehavior.state = BottomSheetBehavior.STATE_COLLAPSED

        update(
            Crew(
                id = navArgs.id,
                image = navArgs.url,
                name = null,
                status = null,
                agency = null,
                wikipedia = null,
                role = null
            )
        )

        viewModel.crew.observe(viewLifecycleOwner) { result ->
            result.data?.find { it.id == navArgs.id }?.let { update(it) }
        }

        viewModel.get()

        binding.close.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun update(person: Crew) {
        binding.itemCrewConstraint.transitionName = person.id

        Glide.with(requireContext()).load(person.image).into(binding.crewImage)

        binding.crewName.text = person.name
        person.status?.let { status ->
            binding.crewStatus.text = when (status) {
                CrewStatus.ACTIVE -> SPACEX_CREW_STATUS_ACTIVE
                CrewStatus.INACTIVE -> SPACEX_CREW_STATUS_INACTIVE
                CrewStatus.RETIRED -> SPACEX_CREW_STATUS_RETIRED
                CrewStatus.UNKNOWN -> SPACEX_CREW_STATUS_UNKNOWN
            }.replaceFirstChar { it.uppercase() }
        }
        binding.crewAgency.text = person.agency

        person.launches?.let { launches ->
            binding.missionsRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CrewMissionsAdapter().also { it.submitList(launches) }
            }
            if (launches.isEmpty()) binding.crewMissionLabel.visibility = View.GONE
        }
    }
}
