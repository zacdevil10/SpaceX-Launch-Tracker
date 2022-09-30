package uk.co.zac_h.spacex.statistics.graphs.launchhistory.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import uk.co.zac_h.spacex.databinding.FragmentLaunchHistoryFilterBinding
import uk.co.zac_h.spacex.types.LaunchHistoryFilter
import uk.co.zac_h.spacex.utils.BottomDrawerFragment

class LaunchHistoryFilterFragment : BottomDrawerFragment() {

    private lateinit var binding: FragmentLaunchHistoryFilterBinding

    private val viewModel: LaunchHistoryFilterViewModel by activityViewModels()

    override val container: ConstraintLayout by lazy { binding.container }

    override val scrim: View by lazy { binding.scrim }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchHistoryFilterBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.launchHistoryChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            viewModel.setFilter(
                when (checkedIds.firstOrNull()) {
                    binding.launchHistorySuccessToggle.id -> LaunchHistoryFilter.SUCCESSES
                    binding.launchHistoryFailureToggle.id -> LaunchHistoryFilter.FAILURES
                    else -> null
                }
            )
        }

        binding.reset.setOnClickListener {
            binding.launchHistorySuccessToggle.isChecked = false
            binding.launchHistoryFailureToggle.isChecked = false
        }
    }
}
