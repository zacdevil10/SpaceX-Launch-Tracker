package uk.co.zac_h.spacex.launches.filter

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.core.util.Pair
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.transition.Slide
import com.google.android.material.datepicker.MaterialDatePicker
import com.google.android.material.transition.MaterialContainerTransform
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.ApiResult
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.FragmentLaunchesFilterBinding
import uk.co.zac_h.spacex.launches.adapters.LaunchesAdapter
import uk.co.zac_h.spacex.utils.ToggleableOpenable
import uk.co.zac_h.spacex.utils.formatRange

@AndroidEntryPoint
class LaunchesFilterFragment : Fragment() {

    private val viewModel: LaunchesFilterViewModel by viewModels()

    private lateinit var binding: FragmentLaunchesFilterBinding

    private lateinit var launchesAdapter: LaunchesAdapter

    private val openableFilter: ToggleableOpenable by lazy {
        object : ToggleableOpenable() {

            override fun isOpen(): Boolean = binding.toolbarGroup.visibility == View.VISIBLE

            override fun close() {
                binding.toolbarGroup.visibility = View.GONE
            }

            override fun open() {
                binding.toolbarGroup.visibility = View.VISIBLE
            }

        }
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
            if (result.status == ApiResult.Status.SUCCESS) launchesAdapter.submitList(result.data)
        }

        viewModel.filterClass.search.observe(viewLifecycleOwner) {
            if (binding.searchLayout.editText?.text.isNullOrEmpty()) {
                if (it.isFiltered()) binding.searchLayout.editText?.setText(it.filter.orEmpty())
            }
        }

        viewModel.filterClass.date.observe(viewLifecycleOwner) {
            val range = it.filter?.let { range ->
                Pair(range.first, range.last)
            }

            binding.datePicker.isChecked = it.isFiltered()

            binding.datePicker.text = range?.let {
                "${range.first.formatRange()}-${range.second.formatRange()}"
            } ?: "Select"

            dateRangePicker(range)
        }

        viewModel.getLaunches()

        binding.advancedFilterToggle.setOnClickListener {
            openableFilter.toggle()
        }

        binding.dateRangeClearButton.setOnClickListener {
            viewModel.filterClass.clearDateRange()
            it.visibility = View.GONE
        }
    }

    private fun dateRangePicker(range: Pair<Long, Long>? = null): MaterialDatePicker<Pair<Long, Long>> =
        MaterialDatePicker.Builder.dateRangePicker().apply {
            setTitleText("Select date range")
            if (range != null) setSelection(range)
        }.build().also { datePicker ->
            binding.datePicker.setOnClickListener {
                datePicker.show(childFragmentManager, "date_range_picker_tag")
            }

            binding.dateRangeClearButton.visibility = if (range == null) View.GONE else View.VISIBLE

            datePicker.addOnPositiveButtonClickListener {
                viewModel.filterClass.setRange(it)
            }
            datePicker.addOnCancelListener {
                binding.datePicker.isChecked = range != null
            }
            datePicker.addOnNegativeButtonClickListener {
                binding.datePicker.isChecked = range != null
            }
        }

}