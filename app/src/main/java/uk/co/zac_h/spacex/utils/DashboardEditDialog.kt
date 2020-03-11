package uk.co.zac_h.spacex.utils

import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.co.zac_h.spacex.base.App

class DashboardEditDialog : DialogFragment() {

    private val items = arrayOf("Next Launch", "Latest Launch", "Pinned Launches", "Latest News")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val prefs = (requireActivity().application as App).dashboardPreferencesRepo
        val selected = booleanArrayOf(
            (prefs.visible(prefs.PREFERENCES_NEXT_LAUNCH)[prefs.PREFERENCES_NEXT_LAUNCH]
                ?: true) as Boolean,
            (prefs.visible(prefs.PREFERENCES_LATEST_LAUNCH)[prefs.PREFERENCES_LATEST_LAUNCH]
                ?: true) as Boolean,
            (prefs.visible(prefs.PREFERENCES_PINNED_LAUNCH)[prefs.PREFERENCES_PINNED_LAUNCH]
                ?: true) as Boolean,
            (prefs.visible(prefs.PREFERENCES_LATEST_NEWS)[prefs.PREFERENCES_LATEST_NEWS]
                ?: true) as Boolean
        )

        return MaterialAlertDialogBuilder(requireContext()).setTitle("Customise dashboard")
            .setMultiChoiceItems(items, selected) { _, which, isChecked ->
                prefs.isVisible = mutableMapOf(when (which) {
                    0 -> "next_launch".also {
                        prefs.PREFERENCES_SECTION = prefs.PREFERENCES_NEXT_LAUNCH
                    }
                    1 -> "latest_launch".also {
                        prefs.PREFERENCES_SECTION = prefs.PREFERENCES_LATEST_LAUNCH
                    }
                    2 -> "pinned_launch".also {
                        prefs.PREFERENCES_SECTION = prefs.PREFERENCES_PINNED_LAUNCH
                    }
                    3 -> "latest_news".also {
                        prefs.PREFERENCES_SECTION = prefs.PREFERENCES_LATEST_NEWS
                    }
                    else -> throw IllegalArgumentException("Invalid Option")
                } to isChecked)
            }
            .setNegativeButton("Dismiss") { _, _ -> }.create()
    }

}