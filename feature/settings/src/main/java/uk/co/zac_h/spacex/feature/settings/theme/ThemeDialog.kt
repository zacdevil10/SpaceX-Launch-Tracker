package uk.co.zac_h.spacex.feature.settings.theme

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import dagger.hilt.android.AndroidEntryPoint
import uk.co.zac_h.spacex.feature.settings.R
import uk.co.zac_h.spacex.feature.settings.databinding.DialogThemeAlertBinding
import javax.inject.Inject

@AndroidEntryPoint
class ThemeDialog : BottomSheetDialogFragment() {

    @Inject
    lateinit var preferencesRepo: ThemePreferenceRepository

    private lateinit var binding: DialogThemeAlertBinding

    private val itemsP = arrayOf("Dark theme", "Battery saver only", "Light theme")
    private val itemsQ = arrayOf("Dark theme", "System theme", "Light theme")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = DialogThemeAlertBinding.inflate(inflater, container, false).apply {
        binding = this
    }.root

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        with(binding) {
            dialogThemeAlways.text = getVersionList()[0]
            dialogThemeSystem.text = getVersionList()[1]
            dialogThemeNever.text = getVersionList()[2]

            when (preferencesRepo.themeMode) {
                AppCompatDelegate.MODE_NIGHT_YES -> dialogThemeAlways.isChecked = true
                AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> dialogThemeSystem.isChecked =
                    true

                AppCompatDelegate.MODE_NIGHT_NO -> dialogThemeNever.isChecked = true
            }

            dialogThemeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
                preferencesRepo.isTheme = when (checkedId) {
                    R.id.dialog_theme_always -> AppCompatDelegate.MODE_NIGHT_YES
                    R.id.dialog_theme_system -> {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q)
                            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                        else
                            AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                    }

                    R.id.dialog_theme_never -> AppCompatDelegate.MODE_NIGHT_NO
                    else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
                }
            }
        }
    }

    private fun getVersionList(): Array<String> =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) itemsQ else itemsP
}
