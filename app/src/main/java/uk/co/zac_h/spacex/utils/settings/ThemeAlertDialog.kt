package uk.co.zac_h.spacex.utils.settings

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatDelegate
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import uk.co.zac_h.spacex.R
import uk.co.zac_h.spacex.base.App
import uk.co.zac_h.spacex.databinding.DialogThemeAlertBinding

class ThemeAlertDialog : BottomSheetDialogFragment() {

    private var _binding: DialogThemeAlertBinding? = null
    private val binding get() = _binding!!

    private val itemsP = arrayOf("Always", "Battery Saver only", "Never")
    private val itemsQ = arrayOf("Always", "System theme", "Never")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DialogThemeAlertBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.dialogThemeAlways.text = getVersionList()[0]
        binding.dialogThemeSystem.text = getVersionList()[1]
        binding.dialogThemeNever.text = getVersionList()[2]

        val preferenceRepository = (requireActivity().application as App).preferencesRepo
        when (preferenceRepository.themeMode) {
            AppCompatDelegate.MODE_NIGHT_YES -> binding.dialogThemeAlways.isChecked = true
            AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM, AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY -> binding.dialogThemeSystem.isChecked =
                true
            AppCompatDelegate.MODE_NIGHT_NO -> binding.dialogThemeNever.isChecked = true
        }

        binding.dialogThemeRadioGroup.setOnCheckedChangeListener { group, checkedId ->
            preferenceRepository.isTheme = when (checkedId) {
                R.id.dialog_theme_always -> AppCompatDelegate.MODE_NIGHT_YES
                R.id.dialog_theme_system -> {
                    if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q)
                        AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM
                    else
                        AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY
                }
                R.id.dialog_theme_never -> AppCompatDelegate.MODE_NIGHT_NO
                else -> AppCompatDelegate.MODE_NIGHT_UNSPECIFIED
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getVersionList(): Array<String> =
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.Q) itemsQ else itemsP


}