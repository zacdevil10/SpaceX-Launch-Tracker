package uk.co.zac_h.spacex.utils.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.DialogDashboardEditBinding
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_PREVIOUS_LAUNCH
import uk.co.zac_h.spacex.utils.repo.DashboardObj.PREFERENCES_SECTION

class DashboardEditDialog : BottomSheetDialogFragment() {

    private lateinit var binding: DialogDashboardEditBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogDashboardEditBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = (requireActivity().application as App).dashboardPreferencesRepo

        binding.dialogDashboardEditNext.isChecked =
            (prefs.visible(PREFERENCES_NEXT_LAUNCH)[PREFERENCES_NEXT_LAUNCH] ?: true) as Boolean
        binding.dialogDashboardEditPrevious.isChecked =
            (prefs.visible(PREFERENCES_PREVIOUS_LAUNCH)[PREFERENCES_PREVIOUS_LAUNCH]
                ?: true) as Boolean
        binding.dialogDashboardEditPinned.isChecked =
            (prefs.visible(PREFERENCES_PINNED_LAUNCH)[PREFERENCES_PINNED_LAUNCH] ?: true) as Boolean

        binding.dialogDashboardEditNext.setOnCheckedChangeListener { _, isChecked ->
            prefs.isVisible = mutableMapOf(PREFERENCES_NEXT_LAUNCH.also {
                PREFERENCES_SECTION = PREFERENCES_NEXT_LAUNCH
            } to isChecked)
        }

        binding.dialogDashboardEditPrevious.setOnCheckedChangeListener { _, isChecked ->
            prefs.isVisible = mutableMapOf(PREFERENCES_PREVIOUS_LAUNCH.also {
                PREFERENCES_SECTION = PREFERENCES_PREVIOUS_LAUNCH
            } to isChecked)
        }

        binding.dialogDashboardEditPinned.setOnCheckedChangeListener { _, isChecked ->
            prefs.isVisible = mutableMapOf(PREFERENCES_PINNED_LAUNCH.also {
                PREFERENCES_SECTION = PREFERENCES_PINNED_LAUNCH
            } to isChecked)
        }

    }

}