package uk.co.zac_h.spacex.feature.vehicles.rockets.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.core.common.types.RocketFamily
import uk.co.zac_h.spacex.core.common.types.RocketType
import uk.co.zac_h.spacex.feature.vehicles.R
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentRocketFilterDialogBinding
import uk.co.zac_h.spacex.feature.vehicles.rockets.RocketViewModel

class RocketFilterDialogFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentRocketFilterDialogBinding

    private val viewModel: RocketViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentRocketFilterDialogBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.filter.family.observe(viewLifecycleOwner) {
            when (it.family) {
                RocketFamily.FALCON -> binding.falcon.isChecked = true
                RocketFamily.STARSHIP -> binding.starship.isChecked = true
                RocketFamily.NONE -> {
                    binding.falcon.isChecked = false
                    binding.starship.isChecked = false
                }
            }
        }

        viewModel.filter.type.observe(viewLifecycleOwner) {
            binding.falconOne.isChecked = RocketType.FALCON_ONE in it.rockets.orEmpty()
            binding.falconNine.isChecked = RocketType.FALCON_NINE in it.rockets.orEmpty()
            binding.falconHeavy.isChecked = RocketType.FALCON_HEAVY in it.rockets.orEmpty()
        }

        viewModel.filter.order.observe(viewLifecycleOwner) {
            when (it.order) {
                Order.ASCENDING -> binding.ascending.isChecked = true
                Order.DESCENDING -> binding.descending.isChecked = true
            }
        }

        binding.rocketFilterFamilyChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val family = when {
                R.id.falcon in checkedIds -> RocketFamily.FALCON
                R.id.starship in checkedIds -> RocketFamily.STARSHIP
                else -> RocketFamily.NONE
            }

            if (family == RocketFamily.FALCON) {
                binding.falconOne.isEnabled = true
                binding.falconNine.isEnabled = true
                binding.falconHeavy.isEnabled = true
            } else {
                binding.falconOne.isEnabled = false
                binding.falconNine.isEnabled = false
                binding.falconHeavy.isEnabled = false

                viewModel.filter.filterByRocketType(null)
            }

            viewModel.filter.filterByFamily(family)
        }

        binding.rocketFilterTypeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            val checkedChips = checkedIds.mapNotNull { id ->
                when (id) {
                    R.id.falcon_one -> RocketType.FALCON_ONE
                    R.id.falcon_nine -> RocketType.FALCON_NINE
                    R.id.falcon_heavy -> RocketType.FALCON_HEAVY
                    else -> null
                }
            }

            viewModel.filter.filterByRocketType(checkedChips)
        }

        binding.rocketFilterOrderChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            when {
                R.id.ascending in checkedIds -> viewModel.filter.setOrder(Order.ASCENDING)
                R.id.descending in checkedIds -> viewModel.filter.setOrder(Order.DESCENDING)
            }
        }

        binding.reset.setOnClickListener {
            viewModel.filter.clear()
        }
    }
}
