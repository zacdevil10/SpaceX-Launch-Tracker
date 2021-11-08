package uk.co.zac_h.spacex.launches.filter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.CompoundButton
import android.widget.Toast
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.view.doOnPreDraw
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialContainerTransform
import com.google.android.material.transition.MaterialElevationScale
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.FragmentLaunchesFilterBinding
import uk.co.zac_h.spacex.databinding.LaunchesFilterBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.types.RocketType
import uk.co.zac_h.spacex.utils.*

@AndroidEntryPoint
class LaunchesFilterFragment : Fragment() {

    private val viewModel: LaunchesFilterViewModel by viewModels()

    private lateinit var binding: FragmentLaunchesFilterBinding
    private lateinit var launchFilter: LaunchesFilterBinding

    private lateinit var launchesAdapter: LaunchesAdapter

    private lateinit var filterBehavior: BottomSheetBehavior<ConstraintLayout>

    private var shouldScroll: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        exitTransition = MaterialElevationScale(false)
        reenterTransition = MaterialElevationScale(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchesFilterBinding.inflate(inflater, container, false).apply {
        launchFilter = LaunchesFilterBinding.bind(this.filterBottomSheet)
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        shouldScroll = false

        view.doOnPreDraw { startPostponedEnterTransition() }
        postponeEnterTransition()

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

        filterBehavior = BottomSheetBehavior.from(binding.filterBottomSheet)

        val openableFilter = BottomSheetOpenable(filterBehavior)

        val closeFilterOnBackPressed = BottomSheetBackPressed(filterBehavior)

        filterBehavior.apply {
            state = BottomSheetBehavior.STATE_HIDDEN

            addBottomSheetCallback(BottomDrawerCallback().apply {
                addOnSlideAction(AlphaSlideAction(binding.scrim))
                addOnStateChangedAction(VisibilityStateAction(binding.scrim))
                addOnStateChangedAction(BackPressedStateAction(closeFilterOnBackPressed))
                addOnStateChangedAction(ShowHideFabStateAction(binding.filterFab))
            })

            binding.scrim.setOnClickListener {
                state = BottomSheetBehavior.STATE_HIDDEN
            }
        }

        //Setup launches list
        launchesAdapter = LaunchesAdapter(requireContext())

        binding.list.apply {
            layoutManager = LinearLayoutManager(requireContext())
            setHasFixedSize(true)
            adapter = launchesAdapter
        }

        viewModel.launchesLiveData.observe(viewLifecycleOwner) { result ->
            when (result.status) {
                ApiResult.Status.PENDING -> if (launchesAdapter.itemCount == 0) {
                    binding.progress.show()
                }
                ApiResult.Status.SUCCESS -> {
                    binding.progress.hide()
                    launchesAdapter.submitList(result.data) {
                        if (shouldScroll) binding.list.scrollToPosition(0)
                    }
                }
                ApiResult.Status.FAILURE -> showError(result.error?.message)
            }
        }

        viewModel.getLaunches()

        //Observe filter
        with(viewModel.filter) {
            observe(viewLifecycleOwner) {
                shouldScroll = true
            }

            search.observe(viewLifecycleOwner) {
                if (binding.searchLayout.editText?.text?.toString().orEmpty() != it.filter) {
                    binding.searchLayout.editText?.setText(it.filter)
                }
            }

            date.observe(viewLifecycleOwner) {
                val range = it.filter?.let { range ->
                    Pair(range.first, range.last)
                }

                with(launchFilter) {
                    datePicker.isChecked = it.isFiltered
                    dateRangeClearButton.isChecked = it.isFiltered

                    datePicker.text = range?.let {
                        "${range.first.formatRange()}-${range.second.formatRange()}"
                    } ?: "Select"
                }

                dateRangePicker(range)
            }

            order.observe(viewLifecycleOwner) {
                when (it.order) {
                    Order.ASCENDING -> launchFilter.ascending.isChecked = true
                    Order.DESCENDING -> launchFilter.descending.isChecked = true
                }
            }

            rocketType.observe(viewLifecycleOwner) {
                if (it.rockets.isNullOrEmpty()) with(launchFilter) {
                    falconOneChip.isChecked = false
                    falconNineChip.isChecked = false
                    falconHeavyChip.isChecked = false
                    starshipChip.isChecked = false
                }
            }

            isFiltered.observe(viewLifecycleOwner) {
                launchFilter.reset.isChecked = it
                launchFilter.apply.isChecked = it
            }
        }

        //Setup view
        binding.searchLayout.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.filter.search(text)
        }

        binding.searchLayout.editText?.setOnEditorActionListener { _, id, _ ->
            if (id == EditorInfo.IME_ACTION_SEARCH) {
                requireContext().hideKeyboard(root = view)
                binding.searchLayout.clearFocus()
                true
            } else false
        }

        binding.filterFab.setOnClickListener {
            openableFilter.open()
        }

        with(launchFilter) {
            dateRangeClearButton.setOnClickListener {
                viewModel.filter.clearDateFilter()
                it.visibility = View.GONE
            }

            orderGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
                if (isChecked) when (checkedId) {
                    R.id.ascending -> viewModel.filter.setOrder(Order.ASCENDING)
                    R.id.descending -> viewModel.filter.setOrder(Order.DESCENDING)
                }
            }

            val chipListener = CompoundButton.OnCheckedChangeListener { _, _ ->
                val checkedChips = rocketGroup.checkedChipIds.mapNotNull { chipId ->
                    when (chipId) {
                        R.id.falcon_nine_chip -> RocketType.FALCON_NINE
                        R.id.falcon_heavy_chip -> RocketType.FALCON_HEAVY
                        R.id.falcon_one_chip -> RocketType.FALCON_ONE
                        R.id.starship_chip -> RocketType.STARSHIP
                        else -> null
                    }
                }

                viewModel.filter.filterByRocketType(checkedChips)
            }

            falconOneChip.setOnCheckedChangeListener(chipListener)
            falconNineChip.setOnCheckedChangeListener(chipListener)
            falconHeavyChip.setOnCheckedChangeListener(chipListener)
            starshipChip.setOnCheckedChangeListener(chipListener)

            reset.setOnClickListener {
                viewModel.filter.clear()
            }

            apply.setOnClickListener {
                apply.isChecked = !apply.isChecked
                openableFilter.close()
            }
        }

        //Setup back navigation
        binding.backButton.setOnClickListener {
            findNavController().navigateUp()
        }

        requireActivity().onBackPressedDispatcher.addCallback(
            viewLifecycleOwner,
            closeFilterOnBackPressed
        )
    }

    override fun onResume() {
        super.onResume()
        filterBehavior.state = BottomSheetBehavior.STATE_HIDDEN
        shouldScroll = false
    }

    private fun dateRangePicker(range: Pair<Long, Long>?): MaterialDatePicker<Pair<Long, Long>> =
        MaterialDatePicker.Builder.dateRangePicker().apply {
            setTitleText(getString(R.string.date_picker_label))
            if (range != null) setSelection(range)
        }.build().also { datePicker ->
            launchFilter.datePicker.setOnClickListener {
                datePicker.show(childFragmentManager, "date_range_picker_tag")
            }

            launchFilter.dateRangeClearButton.visibility =
                if (range == null) View.GONE else View.VISIBLE

            datePicker.addOnPositiveButtonClickListener {
                viewModel.filter.filterByDate(it)
            }
            datePicker.addOnCancelListener {
                launchFilter.datePicker.isChecked = range != null
            }
            datePicker.addOnNegativeButtonClickListener {
                launchFilter.datePicker.isChecked = range != null
            }
        }

    private fun showError(error: String?) {
        Toast.makeText(context, error, Toast.LENGTH_SHORT).show()
    }

}