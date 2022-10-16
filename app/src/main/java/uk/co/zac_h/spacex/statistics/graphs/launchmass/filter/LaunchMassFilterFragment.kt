package uk.co.zac_h.spacex.statistics.graphs.launchmass.filter

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.fragment.app.activityViewModels
import uk.co.zac_h.spacex.core.types.LaunchMassViewType
import uk.co.zac_h.spacex.core.types.RocketType
import uk.co.zac_h.spacex.databinding.FragmentLaunchMassFilterBinding
import uk.co.zac_h.spacex.utils.BottomDrawerFragment

class LaunchMassFilterFragment : BottomDrawerFragment() {

    private lateinit var binding: FragmentLaunchMassFilterBinding

    private val viewModel: LaunchMassFilterViewModel by activityViewModels()

    override val container: ConstraintLayout by lazy { binding.container }

    override val scrim: View by lazy { binding.scrim }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentLaunchMassFilterBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.launchMassRocketChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            viewModel.setRocketFilter(
                when (checkedIds.firstOrNull()) {
                    binding.launchMassFalconOneToggle.id -> RocketType.FALCON_ONE
                    binding.launchMassFalconNineToggle.id -> RocketType.FALCON_NINE
                    binding.launchMassFalconHeavyToggle.id -> RocketType.FALCON_HEAVY
                    else -> null
                }
            )
        }

        binding.launchMassTypeChipGroup.setOnCheckedStateChangeListener { _, checkedIds ->
            viewModel.setTypeFilter(
                when (checkedIds.firstOrNull()) {
                    binding.launchMassRocketToggle.id -> LaunchMassViewType.ROCKETS
                    binding.launchMassOrbitToggle.id -> LaunchMassViewType.ORBIT
                    else -> null
                }
            )
        }

        binding.reset.setOnClickListener {
            binding.launchMassFalconOneToggle.isChecked = false
            binding.launchMassFalconNineToggle.isChecked = false
            binding.launchMassFalconHeavyToggle.isChecked = false

            binding.launchMassRocketToggle.isChecked = true
        }
    }
}
