package uk.co.zac_h.spacex.vehicles

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.databinding.FragmentVehiclesFilterBinding
import uk.co.zac_h.spacex.types.Order
import uk.co.zac_h.spacex.utils.BottomDrawerFragment

class VehiclesFilterFragment : BottomDrawerFragment() {

    private lateinit var binding: FragmentVehiclesFilterBinding

    private val viewModel: VehiclesFilterViewModel by activityViewModels()

    override val container: ConstraintLayout by lazy { binding.container }

    override val scrim: View by lazy { binding.scrim }

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