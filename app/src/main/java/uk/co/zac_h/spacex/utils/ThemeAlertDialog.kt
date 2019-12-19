package uk.co.zac_h.spacex.utils

import android.app.Dialog
import android.os.Build
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.DialogFragment
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import uk.co.zac_h.spacex.base.App

class ThemeAlertDialog : DialogFragment() {


    private val itemsP = arrayOf("Always", "Battery Saver only", "Never")
    private val itemsQ = arrayOf("Always", "Automatically", "Never")

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val preferenceRepository = (requireActivity().application as App).preferencesRepo
        val checkedItem = when (preferenceRepository.themeMode) {
            AppCompatDelegate.MODE_NIGHT_NO -> 2
            AppCompatDelegate.MODE_NIGHT_YES -> 0
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> 1
            else -> 0
        }

        return MaterialAlertDialogBuilder(requireContext()).setTitle("Dark theme")
            .setSingleChoiceItems(getVersionList(), checkedItem) { _, which ->
                dismiss()
                preferenceRepository.isTheme = when (which) {
                    0 -> AppCompatDelegate.MODE_NIGHT_YES
                    1 -> {
                        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        else
                            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }
                    2 -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                }
            }
            .setNegativeButton("Dismiss") { _, _ -> }.create()
    }

    private fun getVersionList(): Array<String> =
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) itemsQ else itemsP


}