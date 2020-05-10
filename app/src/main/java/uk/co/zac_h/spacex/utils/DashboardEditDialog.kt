package uk.co.zac_h.spacex.utils

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_NEXT_LAUNCH
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_PINNED_LAUNCH
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_PREVIOUS_LAUNCH
import uk.co.zac_h.spacex.utils.DashboardObj.PREFERENCES_SECTION

class DashboardEditDialog : DialogFragment() {

    private val items =
        arrayOf("Next", "Previous", "Pinned"/*, "Latest News"*/)

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefs = (requireActivity().application as App).dashboardPreferencesRepo
        val selected = booleanArrayOf(
            (prefs.visible(PREFERENCES_NEXT_LAUNCH)[PREFERENCES_NEXT_LAUNCH]
                ?: true) as Boolean,
            (prefs.visible(PREFERENCES_PREVIOUS_LAUNCH)[PREFERENCES_PREVIOUS_LAUNCH]
                ?: true) as Boolean,
            (prefs.visible(PREFERENCES_PINNED_LAUNCH)[PREFERENCES_PINNED_LAUNCH]
                ?: true) as Boolean/*,
            (prefs.visible(PREFERENCES_LATEST_NEWS)[PREFERENCES_LATEST_NEWS]
                ?: true) as Boolean*/
        )

        return MaterialAlertDialogBuilder(requireContext()).setTitle("Customise dashboard")
            .setMultiChoiceItems(items, selected) { _, which, isChecked ->
                prefs.isVisible = mutableMapOf(when (which) {
                    0 -> "next_launch".also {
                        PREFERENCES_SECTION = PREFERENCES_NEXT_LAUNCH
                    }
                    1 -> "previous_launch".also {
                        PREFERENCES_SECTION = PREFERENCES_PREVIOUS_LAUNCH
                    }
                    2 -> "pinned_launch".also {
                        PREFERENCES_SECTION = PREFERENCES_PINNED_LAUNCH
                    }
                    /*3 -> "latest_news".also {
                        PREFERENCES_SECTION = PREFERENCES_LATEST_NEWS
                    }*/
                    else -> throw IllegalArgumentException("Invalid Option")
                } to isChecked)
            }
            .setNegativeButton("Dismiss") { _, _ -> }.create()
    }

}