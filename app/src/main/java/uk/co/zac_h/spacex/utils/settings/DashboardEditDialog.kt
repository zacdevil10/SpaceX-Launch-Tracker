package uk.co.zac_h.spacex.utils.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.PREFERENCES_PREVIOUS_LAUNCH
import uk.co.zac_h.spacex.dashboard.DashboardViewModel
import uk.co.zac_h.spacex.databinding.DialogDashboardEditBinding

@AndroidEntryPoint
class DashboardEditDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDashboardEditBinding

    private val viewModel: DashboardViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogDashboardEditBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogDashboardEditNext.isChecked =
            viewModel.getDashboardSectionState(PREFERENCES_NEXT_LAUNCH)

        binding.dialogDashboardEditPrevious.isChecked =
            viewModel.getDashboardSectionState(PREFERENCES_PREVIOUS_LAUNCH)

        binding.dialogDashboardEditPinned.isChecked =
            viewModel.getDashboardSectionState(PREFERENCES_PINNED_LAUNCH)

        binding.dialogDashboardEditNext.setOnCheckedChangeListener { _, isChecked ->
            update(PREFERENCES_NEXT_LAUNCH, isChecked)
        }

        binding.dialogDashboardEditPrevious.setOnCheckedChangeListener { _, isChecked ->
            update(PREFERENCES_PREVIOUS_LAUNCH, isChecked)
        }

        binding.dialogDashboardEditPinned.setOnCheckedChangeListener { _, isChecked ->
            update(PREFERENCES_PINNED_LAUNCH, isChecked)
        }

    }

    private fun update(id: String, isChecked: Boolean) {
        if (isChecked) viewModel.showDashboardSection(id) else viewModel.hideDashboardSection(id)
    }
}
