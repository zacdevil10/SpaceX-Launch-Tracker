package uk.co.zac_h.spacex.about.history.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.core.types.Order
import uk.co.zac_h.spacex.databinding.FragmentHistoryFilterBinding
import uk.co.zac_h.spacex.utils.BottomDrawerFragment

class HistoryFilterFragment : BottomDrawerFragment() {

    private lateinit var binding: FragmentHistoryFilterBinding

    private val viewModel: HistoryFilterViewModel by activityViewModels()

    override val container: ConstraintLayout by lazy { binding.container }

    override val scrim: View by lazy { binding.scrim }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentHistoryFilterBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.historyFilterOrderButtonGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) when (checkedId) {
                R.id.ascending -> viewModel.setOrder(Order.ASCENDING)
                R.id.descending -> viewModel.setOrder(Order.DESCENDING)
            }
        }

        viewModel.order.observe(viewLifecycleOwner) {
            when (it) {
                Order.ASCENDING -> binding.ascending.isChecked = true
                Order.DESCENDING, null -> binding.descending.isChecked = true
            }
        }

        binding.reset.setOnClickListener {
            viewModel.reset()
        }
    }
}
