package uk.co.zac_h.spacex.feature.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.navGraphViewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.core.common.types.Order
import uk.co.zac_h.spacex.feature.vehicles.databinding.FragmentVehiclesFilterBinding

@AndroidEntryPoint
class VehiclesFilterFragment : BottomSheetDialogFragment() {

    private lateinit var binding: FragmentVehiclesFilterBinding

    private val viewModel: VehiclesFilterViewModel by navGraphViewModels(R.id.vehicles_nav_graph) {
        defaultViewModelProviderFactory
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentVehiclesFilterBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.vehiclesFilterOrderButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) when (checkedId) {
                R.id.ascending -> viewModel.setOrder(Order.ASCENDING)
                R.id.descending -> viewModel.setOrder(Order.DESCENDING)
            }
        }

        viewModel.pageOrder.observe(viewLifecycleOwner) {
            when (it) {
                Order.ASCENDING, null -> binding.ascending.isChecked = true
                Order.DESCENDING -> binding.descending.isChecked = true
            }
        }

        binding.reset.setOnClickListener {
            viewModel.reset()
        }
    }
}
