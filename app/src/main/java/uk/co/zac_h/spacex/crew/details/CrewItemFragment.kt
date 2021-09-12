package uk.co.zac_h.spacex.crew.details

import android.os.Bundle
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.marginTop
import androidx.navigation.fragment.navArgs
import androidx.navigation.navGraphViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.transition.MaterialContainerTransform
import uk.co.zac_h.spacex.*
import uk.co.zac_h.spacex.base.BaseFragment
import uk.co.zac_h.spacex.crew.CrewViewModel
import uk.co.zac_h.spacex.crew.adapters.CrewMissionsAdapter
import uk.co.zac_h.spacex.databinding.FragmentCrewItemBinding
import uk.co.zac_h.spacex.dto.spacex.Crew
import uk.co.zac_h.spacex.dto.spacex.CrewStatus
import kotlin.math.roundToInt

class CrewItemFragment : BaseFragment() {

    override var title: String = "Crew"

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

        with(binding) {
            itemCrewConstraint.transitionName = navArgs.id

            val typedVal = TypedValue()
            val initialMargin = indicator.marginTop
            val bottomSheetBehavior = BottomSheetBehavior.from(crewBottomSheet)

            bottomSheetBehavior.apply {
                state = BottomSheetBehavior.STATE_COLLAPSED

                addBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback() {
                    override fun onSlide(bottomSheet: View, slideOffset: Float) {
                        requireContext().theme.resolveAttribute(
                            android.R.attr.actionBarSize,
                            typedVal,
                            true
                        ).let {
                            val actionBarHeight = TypedValue.complexToDimensionPixelSize(
                                typedVal.data,
                                requireContext().resources.displayMetrics
                            )

                            indicator.layoutParams =
                                (indicator.layoutParams as ConstraintLayout.LayoutParams).apply {
                                    topMargin =
                                        (initialMargin + (actionBarHeight * slideOffset)).roundToInt()
                                }

                        }
                    }

                    override fun onStateChanged(bottomSheet: View, newState: Int) {

                    }
                })
            }
        }

        viewModel.crew.observe(viewLifecycleOwner) { result ->
            result.data?.find { it.id == navArgs.id }?.let { update(it) }
        }
    }

    private fun update(person: Crew) {
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

        person.launches?.let {
            binding.missionsRecycler.apply {
                layoutManager = LinearLayoutManager(context)
                adapter = CrewMissionsAdapter(context, it)
            }
            if (it.isEmpty()) binding.crewMissionLabel.visibility = View.GONE
        }
    }
}