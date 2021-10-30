package uk.co.zac_h.spacex.launches.filter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import android.widget.FrameLayout
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.FragmentLaunchesFilterBinding
import uk.co.zac_h.spacex.dto.spacex.RocketType
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.utils.*

@AndroidEntryPoint
class LaunchesFilterFragment : Fragment() {

    private val viewModel: LaunchesFilterViewModel by viewModels()

    private lateinit var binding: FragmentLaunchesFilterBinding

    private lateinit var launchesAdapter: LaunchesAdapter

    private val filterBehavior: BottomSheetBehavior<FrameLayout> by lazy {
        BottomSheetBehavior.from(binding.filterBottomSheet)
    }

    private val openableFilter by lazy { BottomSheetOpenable(filterBehavior) }

    private val closeFilterOnBackPressed by lazy {
        BottomSheetBackPressed(filterBehavior)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesFilterBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enterTransition = MaterialContainerTransform().apply {
            startView = requireActivity().findViewById(R.id.fab)
            endView = binding.container
            scrimColor = Color.TRANSPARENT
            containerColor = ContextCompat.getColor(requireContext(), R.color.color_background)
            startContainerColor = ContextCompat.getColor(requireContext(), R.color.color_secondary)
            endContainerColor = ContextCompat.getColor(requireContext(), R.color.color_background)
        }
        returnTransition = Slide().apply {
            addTarget(binding.container)
        }

        filterBehavior.state = BottomSheetBehavior.STATE_HIDDEN

        filterBehavior.addBottomSheetCallback(BottomDrawerCallback().apply {
            addOnSlideAction(AlphaSlideAction(binding.scrim))
            addOnStateChangedAction(VisibilityStateAction(binding.scrim))
            addOnStateChangedAction(BackPressedStateAction(closeFilterOnBackPressed))
            addOnStateChangedAction(FabVisibilityStateAction(binding.filterFab))
        })

        binding.scrim.setOnClickListener {
            filterBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        }

        launchesAdapter = LaunchesAdapter(requireContext())

        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.searchLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.filterClass.updateSearch(text)
        }

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        viewModel.launchesLiveData.observe(viewLifecycleOwner) { result ->
            if (result.status == ApiResult.Status.SUCCESS) launchesAdapter.submitList(result.data) {
                binding.list.scrollToPosition(0)
            }
        }

        viewModel.filterClass.search.observe(viewLifecycleOwner) {
            if (binding.searchLayout.editText?.text.isNullOrEmpty()) {
                if (it.isFiltered) binding.searchLayout.editText?.setText(it.filter.orEmpty())
            }
        }

        viewModel.filterClass.date.observe(viewLifecycleOwner) {
            val range = it.filter?.let { range ->
                Pair(range.first, range.last)
            }

            binding.launchesFilter.datePicker.isChecked = it.isFiltered
            binding.launchesFilter.dateRangeClearButton.isChecked = it.isFiltered

            binding.launchesFilter.datePicker.text = range?.let {
                "${range.first.formatRange()}-${range.second.formatRange()}"
            } ?: "Select"

            dateRangePicker(range)
        }

        viewModel.filterClass.order.observe(viewLifecycleOwner) {
            when (it.order) {
                FilterOrder.ASCENDING -> binding.launchesFilter.ascending.isChecked = true
                FilterOrder.DESCENDING -> binding.launchesFilter.descending.isChecked = true
            }
        }

        viewModel.filterClass.rocketType.observe(viewLifecycleOwner) {
            if (it.rockets.isNullOrEmpty()) {
                binding.launchesFilter.falconOneChip.isChecked = false
                binding.launchesFilter.falconNineChip.isChecked = false
                binding.launchesFilter.falconHeavyChip.isChecked = false
                binding.launchesFilter.starshipChip.isChecked = false
            }
        }

        viewModel.getLaunches()

        binding.filterFab.setOnClickListener {
            openableFilter.open()
        }

        binding.launchesFilter.dateRangeClearButton.setOnClickListener {
            viewModel.filterClass.clearDateRange()
            it.visibility = View.GONE
        }

        binding.launchesFilter.orderGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.ascending -> viewModel.filterClass.setOrder(FilterOrder.ASCENDING)
                    R.id.descending -> viewModel.filterClass.setOrder(FilterOrder.DESCENDING)
                }
            }
        }

        val chipListener = CompoundButton.OnCheckedChangeListener { _, _ ->
            val rockets: List<RocketType> =
                binding.launchesFilter.rocketGroup.checkedChipIds.mapNotNull {
                    when (it) {
                        R.id.falcon_nine_chip -> RocketType.FALCON_NINE
                        R.id.falcon_heavy_chip -> RocketType.FALCON_HEAVY
                        R.id.falcon_one_chip -> RocketType.FALCON_ONE
                        R.id.starship_chip -> RocketType.STARSHIP
                        else -> null
                    }
                }

            viewModel.filterClass.setRockets(rockets)
        }

        binding.launchesFilter.falconOneChip.setOnCheckedChangeListener(chipListener)
        binding.launchesFilter.falconNineChip.setOnCheckedChangeListener(chipListener)
        binding.launchesFilter.falconHeavyChip.setOnCheckedChangeListener(chipListener)
        binding.launchesFilter.starshipChip.setOnCheckedChangeListener(chipListener)

        binding.resetFab.setOnClickListener {
            viewModel.filterClass.reset()
            binding.searchLayout.editText?.setText("")
        }

        viewModel.filterClass.isFiltered.observe(viewLifecycleOwner) {
            if (it) binding.resetFab.show() else binding.resetFab.hide()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            closeFilterOnBackPressed
        )
    }

    private fun dateRangePicker(range: Pair<Long, Long>? = null): MaterialDatePicker<Pair<Long, Long>> =
        MaterialDatePicker.Builder.dateRangePicker().apply {
            setTitleText("Select date range")
            if (range != null) setSelection(range)
        }.build().also { datePicker ->
            binding.launchesFilter.datePicker.setOnClickListener {
                datePicker.show(childFragmentManager, "date_range_picker_tag")
            }

            binding.launchesFilter.dateRangeClearButton.visibility =
                if (range == null) View.GONE else View.VISIBLE

            datePicker.addOnPositiveButtonClickListener {
                viewModel.filterClass.setRange(it)
            }
            datePicker.addOnCancelListener {
                binding.launchesFilter.datePicker.isChecked = range != null
            }
            datePicker.addOnNegativeButtonClickListener {
                binding.launchesFilter.datePicker.isChecked = range != null
            }
        }

}